package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GitHubNotification(
    val id: String,
    val subject: Subject,
    val repository: Repository,
) {
    @Serializable
    data class Subject(
        val title: String,
        val url: String,
        val type: String,
    )

    @Serializable
    data class Repository(
        val fullName: String,
    )
}
