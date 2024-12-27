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
            val htmlUrl = apiUrlToHtmlUrlConverter(
                htmlUrl = it.repository.htmlUrl,
                subjectUrl = it.subject.url,
                type = it.subject.type,
            )
            TableDataDto(
                it.subject.title,
                htmlUrl,
            )
        }
    }

    fun apiUrlToHtmlUrlConverter(htmlUrl: String, subjectUrl: String, type: String): URL {
        val number = subjectUrl.split("/").last()
        return URI("$htmlUrl/${TYPE_TO_PATH[type]}/$number").toURL()
    }
}
