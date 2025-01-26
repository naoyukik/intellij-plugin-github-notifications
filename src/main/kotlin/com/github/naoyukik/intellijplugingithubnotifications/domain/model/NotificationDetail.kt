package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetail(
    val state: String = "",
    val merged: Boolean = false,
    val draft: Boolean = false,
    @SerialName("html_url")
    val htmlUrl: String,
) {
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
