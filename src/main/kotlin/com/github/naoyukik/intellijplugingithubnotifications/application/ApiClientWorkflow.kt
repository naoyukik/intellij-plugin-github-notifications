package com.github.naoyukik.intellijplugingithubnotifications.application

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification.SubjectType
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetail
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetailError
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState
import com.intellij.openapi.util.IconLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URL
import java.time.ZonedDateTime
import javax.swing.Icon

class ApiClientWorkflow(
    private val repository: GitHubNotificationRepository,
    private val settingStateRepository: SettingStateRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private var latestFetchTime: ZonedDateTime? = null

    companion object {
        private val TYPE_TO_PATH = mapOf(
            "PullRequest" to "pull",
            "Issue" to "issues",
            "Release" to "releases",
        )

        private val issuesOpened = IconLoader.getIcon(
            "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-opened-16.svg",
            this::class.java.classLoader,
        )

        private val issuesClosed = IconLoader.getIcon(
            "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-closed-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsOpen = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsMerged = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-merge-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsClosed = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-closed-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsDraft = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-draft-16.svg",
            this::class.java.classLoader,
        )

        private val release = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/tag-16.svg",
            this::class.java.classLoader,
        )

        private val unread = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/unread-16.svg",
            this::class.java.classLoader,
        )

        private val TYPE_TO_EMOJI: Map<String, Icon> = mapOf(
            "PullRequestOpen" to pullRequestsOpen,
            "PullRequestMerged" to pullRequestsMerged,
            "PullRequestClosed" to pullRequestsClosed,
            "PullRequestDraft" to pullRequestsDraft,
            "IssueOpen" to issuesOpened,
            "IssueClosed" to issuesClosed,
            "Release" to release,
            "Unread" to unread,
        )
    }

    suspend fun fetchNotifications(): List<TableDataDto> = withContext(dispatcher) {
        val settingState = settingStateRepository.loadSettingState()
        val ghCliPath = settingState.ghCliPath
        val includingRead = settingState.includingRead
        if (!hasNewNotificationsSinceLastCheck(settingState)) return@withContext emptyList()

        val notifications = settingState.repositoryName.takeUnless { it.isEmpty() }?.let { nonEmptyRepositoryName ->
            repository.fetchNotificationsByRepository(ghCliPath, nonEmptyRepositoryName, includingRead)
        } ?: repository.fetchNotifications(ghCliPath, includingRead)

        notifications.takeIf { it.isNotEmpty() } ?: return@withContext emptyList()

        val updatedNotifications = notifications.map { notification ->
            val detailAPiPath = setDetailApiPath(notification)
            val updatedNotification = detailAPiPath?.let {
                val detail = repository.fetchNotificationsReleaseDetail(
                    ghCliPath = ghCliPath,
                    repositoryName = notification.repository.fullName,
                    detailApiPath = it,
                )
                when (detail) {
                    is NotificationDetail -> {
                        notification.copy(
                            subject = notification.subject.copy(
                                url = detail.htmlUrl,
                            ),
                            detail = detail,
                        )
                    }
                    is NotificationDetailError -> null
                }
            } ?: notification
            updatedNotification
        }

        updatedNotifications.toTableDataDto()
    }

    private fun hasNewNotificationsSinceLastCheck(
        settingState: SettingState,
    ): Boolean {
        val previousFetchTime = latestFetchTime ?: run {
            latestFetchTime = ZonedDateTime.now()
            return true
        }
        latestFetchTime = ZonedDateTime.now()
        val ghCliPath = settingState.ghCliPath
        val includingRead = settingState.includingRead
        val latestNotifications = settingState.repositoryName.takeUnless { repositoryName ->
            repositoryName.isEmpty()
        }?.let { nonEmptyRepositoryName ->
            repository.fetchLatestNotificationsByRepository(
                ghCliPath = ghCliPath,
                repositoryName = nonEmptyRepositoryName,
                previousTime = previousFetchTime,
                includingRead = includingRead,
            )
        } ?: repository.fetchLatestNotifications(
            ghCliPath = ghCliPath,
            previousTime = previousFetchTime,
            includingRead = includingRead,
        )
        return latestNotifications.isNotEmpty()
    }

    private fun setDetailApiPath(notification: GitHubNotification): String? {
        return when (val type = notification.subject.type) {
            SubjectType.Release, SubjectType.Issue, SubjectType.PullRequest ->
                "${type.setApiPath()}/${setDetailId(notification)}}"
            SubjectType.UNKNOWN -> null
        }
    }

    private fun setDetailId(notification: GitHubNotification): String? {
        return when (notification.subject.type) {
            SubjectType.Release, SubjectType.Issue, SubjectType.PullRequest -> {
                val detailId = URI(
                    notification.subject.url,
                ).path.substringAfterLast("/")
                detailId.ifEmpty { null }
            }
            SubjectType.UNKNOWN -> null
        }
    }

    private fun List<GitHubNotification>.toTableDataDto(): List<TableDataDto> {
        return this.map {
            val issueNumber = getIssueNumber(it.subject.url)
            TableDataDto(
                title = it.subject.title,
                fullName = apiUrlToRepositoryIssueNumberConverter(
                    repositoryFullName = it.repository.fullName,
                    issueNumber = issueNumber,
                    type = it.subject.type,
                ),
                htmlUrl = apiUrlToHtmlUrlConverter(
                    htmlUrl = it.repository.htmlUrl,
                    issueNumber = issueNumber,
                    type = it.subject.type,
                ),
                reason = it.reason,
                updatedAt = it.updatedAt,
                typeEmoji = apiUrlToEmojiConverter(it),
                reviewers = it.detail?.requestedReviewers?.map { reviewer -> reviewer.login } ?: emptyList(),
                unreadEmoji = it.unread.takeIf { it }?.let { TYPE_TO_EMOJI["Unread"] },
            )
        }
    }

    private fun apiUrlToRepositoryIssueNumberConverter(
        repositoryFullName: String,
        issueNumber: String,
        type: SubjectType,
    ): String {
        return TYPE_TO_PATH[type.name]?.let { typeToPath ->
            when (typeToPath) {
                "issues", "pull" -> "$repositoryFullName #$issueNumber"
                "releases" -> "$issueNumber in $repositoryFullName"
                else -> "$repositoryFullName #$issueNumber"
            }
        } ?: "$repositoryFullName #$issueNumber"
    }

    private fun apiUrlToHtmlUrlConverter(htmlUrl: String, issueNumber: String, type: SubjectType): URL? {
        return TYPE_TO_PATH[type.name]?.let { typeToPath ->
            when (typeToPath) {
                "issues", "pull" -> URI("$htmlUrl/$typeToPath/$issueNumber").toURL()
                "releases" -> URI("$htmlUrl/$typeToPath/tag/$issueNumber").toURL()
                else -> null
            }
        }
    }

    @Suppress("ComplexMethod")
    private fun apiUrlToEmojiConverter(notification: GitHubNotification): Icon? {
        return when (val type = notification.subject.type) {
            SubjectType.PullRequest -> notification.detail?.let { detail ->
                return when {
                    detail.isPullRequestDraft() -> TYPE_TO_EMOJI["PullRequestDraft"]
                    detail.isPullRequestClosed() -> TYPE_TO_EMOJI["PullRequestClosed"]
                    detail.isPullRequestMerged() -> TYPE_TO_EMOJI["PullRequestMerged"]
                    detail.isPullRequestOpen() -> TYPE_TO_EMOJI["PullRequestOpen"]
                    else -> null
                }
            }
            SubjectType.Issue -> notification.detail?.let { detail ->
                return when {
                    detail.isIssueClosed() -> TYPE_TO_EMOJI["IssueClosed"]
                    detail.isIssueOpen() -> TYPE_TO_EMOJI["IssueOpen"]
                    else -> null
                }
            }
            SubjectType.Release -> TYPE_TO_EMOJI[type.name]
            SubjectType.UNKNOWN -> null
        }
    }

    private fun getIssueNumber(url: String): String = url.split("/").last()
}
