package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.NotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubToken
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NotificationRepositoryImpl : NotificationRepository {
    private val client = OkHttpClient()

    override fun fetchNotifications(token: GitHubToken): List<GitHubNotification> {
        val request = Request.Builder()
            .url("https://api.github.com/notifications")
            .header("Authorization", "Bearer ${token.value}")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.toGitHubNotification() ?: emptyList()
        }
    }

    private fun okhttp3.ResponseBody.toGitHubNotification(): List<GitHubNotification> {
        val responseJson = this.string()
        return Json.decodeFromString(responseJson)
    }
}
