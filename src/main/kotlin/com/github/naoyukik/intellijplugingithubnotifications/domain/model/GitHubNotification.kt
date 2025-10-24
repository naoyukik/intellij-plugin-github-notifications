package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubNotification(
    val id: String,
    val unread: Boolean,
    val subject: Subject,
    val repository: Repository,
    val reason: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val detail: NotificationDetail? = null,
) {
    @Serializable
    data class Subject(
        val title: String,
        val url: String?,
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
