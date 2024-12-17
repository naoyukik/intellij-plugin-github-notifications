package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubToken

interface NotificationRepository {
    fun fetchNotifications(token: GitHubToken): List<GitHubNotification>
    fun fetchNotificationsMock(): List<GitHubNotification>
}
