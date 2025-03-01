package com.github.naoyukik.intellijplugingithubnotifications.domain.model
enum class NotificationType(val displayName: String) {
    PULL_REQUEST_OPEN("Pull Request Open"),
    PULL_REQUEST_MERGED("Pull Request Merged"),
    PULL_REQUEST_CLOSED("Pull Request Closed"),
    PULL_REQUEST_DRAFT("Pull Request Draft"),
    ISSUE_OPEN("Issue Open"),
    ISSUE_CLOSED("Issue Closed"),
    Release("RELEASE"),
    Unread("UNREAD"),
}
