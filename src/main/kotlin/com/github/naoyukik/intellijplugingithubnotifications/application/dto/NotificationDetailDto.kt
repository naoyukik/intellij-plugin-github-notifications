package com.github.naoyukik.intellijplugingithubnotifications.application.dto

sealed class NotificationDetailDto {
    data class NotificationDetail(
        val state: String = "",
        val merged: Boolean = false,
        val draft: Boolean = false,
        val htmlUrl: String,
        val requestedReviewers: List<RequestedReviewers> = emptyList(),
    ) : NotificationDetailDto() {
        data class RequestedReviewers(
            val login: String,
        )

        fun isPullRequestOpen(): Boolean {
            return this.state == "open"
        }
        fun isPullRequestMerged(): Boolean {
            return this.merged
        }
        fun isPullRequestClosed(): Boolean {
            return this.state == "closed" && !this.merged
        }
        fun isPullRequestDraft(): Boolean {
            return this.draft
        }

        fun isIssueOpen(): Boolean {
            return this.state == IssueState.Open.name.lowercase()
        }
        fun isIssueClosed(): Boolean {
            return this.state == IssueState.Closed.name.lowercase()
        }

        enum class IssueState {
            Open,
            Closed,
        }
    }
}
