package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.utility.CommandExecutor
import kotlinx.serialization.json.Json

class GitHubNotificationRepositoryImpl : GitHubNotificationRepository {
    override fun fetchNotifications(): List<GitHubNotification> {
        val commandResult = CommandExecutor.execute(
            listOf(
                "gh",
                "api",
                "/notifications",
            ),
        )
        return toGitHubNotification(commandResult)
    }

    override fun fetchNotificationsByRepository(repositoryName: String): List<GitHubNotification> {
        val commandResult = CommandExecutor.execute(
            listOf(
                "gh",
                "api",
                "/repos/$repositoryName/notifications",
            ),
        )
        return toGitHubNotification(commandResult)
    }

    private fun toGitHubNotification(jsonString: String?): List<GitHubNotification> {
        return jsonString?.run {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(this)
        } ?: emptyList()
    }
}
