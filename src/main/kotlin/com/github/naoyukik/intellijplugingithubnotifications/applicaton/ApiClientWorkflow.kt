package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.NotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubToken

class ApiClientWorkflow(
    private val repository: NotificationRepository,
) {
    fun fetchNotifications(): List<TableDataDto> {
        // val token = getToken()
        val notifications = repository.fetchNotificationsMock()
        return notifications.toTableDataDto()
    }

    private fun List<GitHubNotification>.toTableDataDto(): List<TableDataDto> {
        return this.map {
            TableDataDto(
                it.subject.title,
            )
        }
    }

    private fun getToken(): GitHubToken = GitHubToken(
        "String",
    )
}
