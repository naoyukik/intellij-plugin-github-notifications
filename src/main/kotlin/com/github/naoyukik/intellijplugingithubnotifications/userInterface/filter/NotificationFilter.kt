package com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel.NotificationType

data class NotificationFilter(
    val type: String?,
    val reviewer: String?,
) {
    companion object {
        fun applyFilter(
            notifications: List<GitHubNotificationDto>,
            filter: NotificationFilter,
        ): List<GitHubNotificationDto> {
            notifications.filter { notification ->
                val typeMatches = when (filter.type) {
                    NotificationType.PULL_REQUEST_OPEN.displayName -> notification.isPullRequestOpen()
                    NotificationType.PULL_REQUEST_MERGED.displayName -> notification.isPullRequestMerged()
                    NotificationType.PULL_REQUEST_CLOSED.displayName -> notification.isPullRequestClosed()
                    NotificationType.PULL_REQUEST_DRAFT.displayName -> notification.isPullRequestDraft()
                    NotificationType.ISSUE_OPEN.displayName -> notification.isIssueOpen()
                    NotificationType.ISSUE_CLOSED.displayName -> notification.isIssueClosed()
                    NotificationType.Release.displayName ->
                        notification.subject.type == GitHubNotificationDto.SubjectType.Release
                    else -> true
                }

                val reviewerMatches = filter.reviewer.isNullOrBlank() || notification.detail?.requestedReviewers?.any {
                        reviewer ->
                    reviewer.login.lowercase() == filter.reviewer.lowercase()
                } == true

                typeMatches && reviewerMatches
            }
        }
    }
}
