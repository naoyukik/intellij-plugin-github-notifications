package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse
import java.time.ZonedDateTime

interface GitHubNotificationRepository {
    fun fetchNotifications(
        ghCliPath: String,
        includingRead: Boolean,
        resultLimit: Int,
    ): List<GitHubNotification>
    fun fetchNotificationsByRepository(
        ghCliPath: String,
        repositoryName: String,
        includingRead: Boolean,
        resultLimit: Int,
    ): List<GitHubNotification>
    fun fetchNotificationsReleaseDetail(
        ghCliPath: String,
        repositoryName: String,
        detailApiPath: String,
    ): NotificationDetailResponse
    fun fetchLatestNotifications(
        ghCliPath: String,
        previousTime: ZonedDateTime,
        includingRead: Boolean,
    ): List<GitHubNotification>
    fun fetchLatestNotificationsByRepository(
        ghCliPath: String,
        repositoryName: String,
        previousTime: ZonedDateTime,
        includingRead: Boolean,
    ): List<GitHubNotification>
}
