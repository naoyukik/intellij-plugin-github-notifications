package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetail
import java.time.ZonedDateTime

interface GitHubNotificationRepository {
    fun fetchNotifications(ghCliPath: String): List<GitHubNotification>
    fun fetchNotificationsByRepository(ghCliPath: String, repositoryName: String): List<GitHubNotification>
    fun fetchNotificationsReleaseDetail(
        ghCliPath: String,
        repositoryName: String,
        detailApiPath: String,
    ): NotificationDetail
    fun fetchLatestNotifications(
        ghCliPath: String,
        previousTime: ZonedDateTime,
    ): List<GitHubNotification>
    fun fetchLatestNotificationsByRepository(
        ghCliPath: String,
        repositoryName: String,
        previousTime: ZonedDateTime,
    ): List<GitHubNotification>
}
