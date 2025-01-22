package com.github.naoyukik.intellijplugingithubnotifications.application

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification.SubjectType
import com.intellij.openapi.util.IconLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URL
import javax.swing.Icon

class ApiClientWorkflow(
    private val repository: GitHubNotificationRepository,
    private val settingStateRepository: SettingStateRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    companion object {
        private val TYPE_TO_PATH = mapOf(
            "PullRequest" to "pull",
            "Issue" to "issues",
            "Release" to "releases",
        )

        private val issues = IconLoader.getIcon(
            "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-opened-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequests = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-16.svg",
            this::class.java.classLoader,
        )

        private val release = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/tag-16.svg",
            this::class.java.classLoader,
        )

        private val TYPE_TO_EMOJI: Map<String, Icon> = mapOf(
            "PullRequest" to pullRequests,
            "Issue" to issues,
            "Release" to release,
        )
    }

    suspend fun fetchNotifications(): List<TableDataDto> = withContext(dispatcher) {
        val settingState = settingStateRepository.loadSettingState()
        val ghCliPath = settingState.ghCliPath
        val notifications = if (settingState.repositoryName.isEmpty()) {
            repository.fetchNotifications(ghCliPath)
        } else {
            repository.fetchNotificationsByRepository(ghCliPath, settingState.repositoryName)
        }

        val updatedNotifications = notifications.map { notification ->
            val detailAPiPath = setDetailApiPath(notification)
            val updatedNotification = if (detailAPiPath != null) {
                val detail = repository.fetchNotificationsReleaseDetail(
                    ghCliPath = ghCliPath,
                    repositoryName = notification.repository.fullName,
                    detailApiPath = detailAPiPath,
                )
                notification.copy(
                    subject = notification.subject.copy(
                        url = detail.htmlUrl,
                    ),
                )
            } else {
                notification
            }
            updatedNotification
        }

        updatedNotifications.toTableDataDto()
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
                if (detailId.isNotEmpty()) detailId else null
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
                typeEmoji = apiUrlToEmojiConverter(it.subject.type),
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

    private fun apiUrlToEmojiConverter(type: SubjectType): Icon? {
        return TYPE_TO_EMOJI[type.name]
    }

    private fun getIssueNumber(url: String): String = url.split("/").last()
}
