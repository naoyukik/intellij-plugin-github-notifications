package com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationType

data class NotificationFilter(
    val type: String?,
) {
    companion object {
        fun applyFilter(
            notifications: List<GitHubNotification>,
            filter: NotificationFilter,
        ): List<GitHubNotification> {
            return notifications.filter {
                when (filter.type) {
                    NotificationType.PULL_REQUEST_OPEN.displayName -> it.detail?.isPullRequestOpen()
                    NotificationType.PULL_REQUEST_MERGED.displayName -> it.detail?.isPullRequestMerged()
                    NotificationType.PULL_REQUEST_CLOSED.displayName -> it.detail?.isPullRequestClosed()
                    NotificationType.PULL_REQUEST_DRAFT.displayName -> it.detail?.isPullRequestDraft()
                    NotificationType.ISSUE_OPEN.displayName -> it.detail?.isIssueOpen()
                    NotificationType.ISSUE_CLOSED.displayName -> it.detail?.isIssueClosed()
                    NotificationType.Release.displayName -> it.subject.type == GitHubNotification.SubjectType.Release
                    else -> true
                } == true
            }
        }
    }
}
