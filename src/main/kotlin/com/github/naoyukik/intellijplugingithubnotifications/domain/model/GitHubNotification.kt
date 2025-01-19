package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubNotification(
    val id: String,
    val subject: Subject,
    val repository: Repository,
    val reason: String,
    @SerialName("updated_at")
    val updatedAt: String,
) {
    @Serializable
    data class Subject(
        val title: String,
        val url: String,
        val type: SubjectType,
    )

    @Serializable
    data class Repository(
        @SerialName("full_name")
        val fullName: String,
        @SerialName("html_url")
        val htmlUrl: String,
    )

    @Serializable
    enum class SubjectType {
        Issue,
        PullRequest,
        Release,
    }
}
