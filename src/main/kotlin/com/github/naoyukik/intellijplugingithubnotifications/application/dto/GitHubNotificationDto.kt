package com.github.naoyukik.intellijplugingithubnotifications.application.dto

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto.NotificationDetail
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto.NotificationDetail.State

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

    private fun isPullRequest(): Boolean {
        return this.subject.type == SubjectType.PullRequest
    }

    fun isPullRequestOpen(): Boolean {
        return isPullRequest() && detail?.state == State.Open.name.lowercase()
    }

    fun isPullRequestMerged(): Boolean {
        return isPullRequest() && detail?.merged == true
    }

    fun isPullRequestClosed(): Boolean {
        return isPullRequest() && detail?.state == State.Closed.name.lowercase() && detail.merged != true
    }

    fun isPullRequestDraft(): Boolean {
        return isPullRequest() && detail?.draft == true
    }

    private fun isIssue(): Boolean {
        return this.subject.type == SubjectType.Issue
    }

    fun isIssueOpen(): Boolean {
        return isIssue() && detail?.state == State.Open.name.lowercase()
    }

    fun isIssueClosed(): Boolean {
        return isIssue() && detail?.state == State.Closed.name.lowercase()
    }

    enum class SubjectType {
        Issue,
        PullRequest,
        Release,
        UNKNOWN,
    }
}
