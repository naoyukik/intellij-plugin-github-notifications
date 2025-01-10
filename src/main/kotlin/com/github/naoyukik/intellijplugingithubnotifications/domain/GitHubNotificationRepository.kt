package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification

interface GitHubNotificationRepository {
    fun fetchNotifications(ghCliPath: String): List<GitHubNotification>
    fun fetchNotificationsByRepository(ghCliPath: String, repositoryName: String): List<GitHubNotification>
}
