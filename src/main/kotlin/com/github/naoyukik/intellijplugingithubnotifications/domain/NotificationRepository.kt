package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification

interface NotificationRepository {
    fun fetchNotifications(): List<GitHubNotification>
}
