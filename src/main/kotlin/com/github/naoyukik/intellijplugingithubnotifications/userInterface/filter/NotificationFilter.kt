package com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel.NotificationType
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel.NotificationUnread

data class NotificationFilter(
    val unread: String?,
    val type: String?,
    val reviewer: String?,
    val label: String?,
) {
    companion object {
        @Suppress("ComplexMethod")
        fun applyFilter(
            notifications: List<GitHubNotificationDto>,
            filter: NotificationFilter,
        ): List<GitHubNotificationDto> {
            return notifications.filter { notification ->
                val unreadMatches = when (filter.unread) {
                    NotificationUnread.UNREAD.displayName -> notification.unread
                    NotificationUnread.READ.displayName -> !notification.unread
                    else -> true
                }

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

                val reviewerMatches = when (filter.reviewer) {
                    "<Choose Reviewer>" -> true
                    else -> {
                        filter.reviewer.isNullOrBlank() || notification.detail?.requestedReviewers?.any { reviewer ->
                            reviewer.login.lowercase() == filter.reviewer.lowercase()
                        } == true
                    }
                }

                val labelMatches = when (filter.label) {
                    "<Choose Label>" -> true
                    else -> {
                        matchesLabelFilter(filter, notification)
                    }
                }

                unreadMatches && typeMatches && reviewerMatches && labelMatches
            }
        }

        private fun matchesLabelFilter(
            filter: NotificationFilter,
            notification: GitHubNotificationDto,
        ): Boolean {
            return if (filter.label?.contains(',') == true) {
                val labels = filter.label.split(",")
                labels.any { label ->
                    notification.detail?.labels?.any { it.name.lowercase() == label.lowercase() } == true
                }
            } else {
                filter.label.isNullOrBlank() || notification.detail?.labels?.any { label ->
                    label.name.lowercase() == filter.label.lowercase()
                } == true
            }
        }
    }
}
