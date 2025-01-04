package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification

interface GitHubNotificationRepository {
    fun fetchNotifications(): List<GitHubNotification>
    fun fetchNotificationsByRepository(repositoryName: String): List<GitHubNotification>
}
