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
        UNKNOWN,
        ;

        fun isRelevantType(): Boolean {
            return when (this) {
                Issue, PullRequest, Release -> true
                UNKNOWN -> false
            }
        }

        fun setApiPath(): String? {
            return when (this) {
                Issue -> "issues"
                PullRequest -> "pulls"
                Release -> "releases"
                UNKNOWN -> null
            }
        }
    }
}
