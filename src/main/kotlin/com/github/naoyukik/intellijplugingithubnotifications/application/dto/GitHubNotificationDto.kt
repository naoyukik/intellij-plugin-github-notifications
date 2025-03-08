package com.github.naoyukik.intellijplugingithubnotifications.application.dto

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto.NotificationDetail

data class GitHubNotificationDto(
    val id: String,
    val unread: Boolean,
    val subject: Subject,
    val repository: Repository,
    val reason: String,
    val updatedAt: String,
    val detail: NotificationDetail? = null,
) {
    data class Subject(
        val title: String,
        val url: String,
        val type: SubjectType,
    )

    data class Repository(
        val fullName: String,
        val htmlUrl: String,
    )

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
