package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URL

class ApiClientWorkflow(
    private val repository: GitHubNotificationRepository,
    private val settingStateRepository: SettingStateRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    companion object {
        val TYPE_TO_PATH = mapOf(
            "PullRequest" to "pull",
            "Issue" to "issues",
        )

        // val pullRequests = IconLoader.getIcon(
        //     "/com/github/naoyukik/intellijplugingithubnotifications/icons/pullRequests.svg",
        //     ApiClientWorkflow::class.java,
        // )
        // val pullRequests = IconManager.getInstance().getIcon(
        //     "com/github/naoyukik/intellijplugingithubnotifications/icons/pullRequests.svg",
        //     this::class.java.classLoader,
        // )

        // val issues = IconManager.getInstance().getIcon(
        //     "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-opened.svg",
        //     this::class.java.classLoader,
        // )
        //
        // val pullRequests = ImageIcon(
        //     javaClass.getResource("/com/github/naoyukik/intellijplugingithubnotifications/icons/pullRequests.svg"),
        // )

        val TYPE_TO_EMOJI = mapOf(
            "PullRequest" to "pullRequests",
            "Issue" to "issues",
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

    private fun apUrlToEmojiConverter(type: String): String? {
        return TYPE_TO_EMOJI[type]
    }

    private fun getIssueNumber(url: String): Int = url.split("/").last().toInt()
}
