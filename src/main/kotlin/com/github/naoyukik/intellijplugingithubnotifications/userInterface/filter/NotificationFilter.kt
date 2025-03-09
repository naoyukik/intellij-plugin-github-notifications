package com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel.NotificationType
import kotlin.collections.filter

data class NotificationFilter(
    val type: String?,
) {
    companion object {
        fun applyFilter(
            notifications: List<GitHubNotificationDto>,
            filter: NotificationFilter,
        ): List<GitHubNotificationDto> {
            return notifications.filter {
                when (filter.type) {
                    NotificationType.PULL_REQUEST_OPEN.displayName -> it.isPullRequestOpen()
                    NotificationType.PULL_REQUEST_MERGED.displayName -> it.isPullRequestMerged()
                    NotificationType.PULL_REQUEST_CLOSED.displayName -> it.isPullRequestClosed()
                    NotificationType.PULL_REQUEST_DRAFT.displayName -> it.isPullRequestDraft()
                    NotificationType.ISSUE_OPEN.displayName -> it.isIssueOpen()
                    NotificationType.ISSUE_CLOSED.displayName -> it.isIssueClosed()
                    NotificationType.Release.displayName -> it.subject.type == GitHubNotificationDto.SubjectType.Release
                    else -> true
                } == true
            }
        }
    }
}
