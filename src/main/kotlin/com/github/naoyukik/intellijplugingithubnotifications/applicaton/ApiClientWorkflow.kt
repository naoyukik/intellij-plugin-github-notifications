package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
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

    suspend fun fetchNotificationsByRepository(): List<TableDataDto> = withContext(dispatcher) {
        val settingState = settingStateRepository.loadSettingState()
        val notifications = if (settingState.repositoryName.isEmpty()) {
            repository.fetchNotifications()
        } else {
            repository.fetchNotificationsByRepository(settingState.repositoryName)
        }
        notifications.toTableDataDto()
    }

    private fun List<GitHubNotification>.toTableDataDto(): List<TableDataDto> {
        return this.map {
            val issueNumber = getIssueNumber(it.subject.url)
            TableDataDto(
                title = it.subject.title,
                fullName = apiUrlToRepositoryIssueNumberConverter(
                    repositoryFullName = it.repository.fullName,
                    issueNumber = issueNumber,
                ),
                htmlUrl = apiUrlToHtmlUrlConverter(
                    htmlUrl = it.repository.htmlUrl,
                    issueNumber = issueNumber,
                    type = it.subject.type,
                ),
                reason = it.reason,
                updatedAt = it.updatedAt,
                typeEmoji = apUrlToEmojiConverter(it.subject.type),
            )
        }
    }

    private fun apiUrlToRepositoryIssueNumberConverter(repositoryFullName: String, issueNumber: Int): String {
        return "$repositoryFullName #$issueNumber"
    }

    private fun apiUrlToHtmlUrlConverter(htmlUrl: String, issueNumber: Int, type: String): URL? {
        return TYPE_TO_PATH[type]?.let { typeToPath ->
            URI("$htmlUrl/$typeToPath/$issueNumber").toURL()
        }
    }

    private fun apUrlToEmojiConverter(type: String): Icon? {
        return TYPE_TO_EMOJI[type]
    }

    private fun getIssueNumber(url: String): Int = url.split("/").last().toInt()
}
