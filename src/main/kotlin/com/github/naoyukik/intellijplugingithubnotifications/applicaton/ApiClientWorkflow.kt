package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.NotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.URL

class ApiClientWorkflow(
    private val repository: NotificationRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    companion object {
        val TYPE_TO_PATH = mapOf(
            "PullRequest" to "pull",
            "Issue" to "issues",
        )
    }

    suspend fun fetchNotifications(): List<TableDataDto> = withContext(dispatcher) {
        val notifications = repository.fetchNotifications()
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

    private fun getIssueNumber(url: String): Int = url.split("/").last().toInt()
}
