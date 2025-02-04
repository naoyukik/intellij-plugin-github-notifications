package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetail
import com.github.naoyukik.intellijplugingithubnotifications.utility.CommandExecutor
import com.github.naoyukik.intellijplugingithubnotifications.utility.DateTimeHandler
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime

class GitHubNotificationRepositoryImpl : GitHubNotificationRepository {
    override fun fetchNotifications(ghCliPath: String): List<GitHubNotification> {
        val commandResult = CommandExecutor.execute(
            listOf(
                ghCliPath,
                "api",
                "/notifications",
            ),
        )
        return toGitHubNotification(commandResult)
    }

    override fun fetchNotificationsByRepository(ghCliPath: String, repositoryName: String): List<GitHubNotification> {
        val commandResult = CommandExecutor.execute(
            listOf(
                ghCliPath,
                "api",
                "/repos/$repositoryName/notifications",
            ),
        )
        return toGitHubNotification(commandResult)
    }

    override fun fetchNotificationsReleaseDetail(
        ghCliPath: String,
        repositoryName: String,
        detailApiPath: String,
    ): NotificationDetail {
        val commandResult = CommandExecutor.execute(
            listOf(
                ghCliPath,
                "api",
                "/repos/$repositoryName/$detailApiPath",
            ),
        )
        return toNotificationReleaseDetail(commandResult)
    }

    override fun fetchLatestNotificationsByRepository(
        ghCliPath: String,
        repositoryName: String,
        previousTime: ZonedDateTime,
    ): List<GitHubNotification> {
        val since = DateTimeHandler.toIso8601(previousTime)
        val commandResult = CommandExecutor.execute(
            listOf(
                ghCliPath,
                "api",
                "/repos/$repositoryName/notifications?since=$since",
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

    private fun toNotificationReleaseDetail(jsonString: String?): NotificationDetail {
        return jsonString?.run {
            val json = Json { ignoreUnknownKeys = true }
            json.decodeFromString(this)
        } ?: throw IllegalArgumentException("No detailed data exists")
    }
}
