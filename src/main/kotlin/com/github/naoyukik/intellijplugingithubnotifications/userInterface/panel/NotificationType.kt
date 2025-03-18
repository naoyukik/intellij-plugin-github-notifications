package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

enum class NotificationType(val displayName: String) {
    UNSELECTED("<Choose type>"),
    PULL_REQUEST_OPEN("Pull Request Open"),
    PULL_REQUEST_MERGED("Pull Request Merged"),
    PULL_REQUEST_CLOSED("Pull Request Closed"),
    PULL_REQUEST_DRAFT("Pull Request Draft"),
    ISSUE_OPEN("Issue Open"),
    ISSUE_CLOSED("Issue Closed"),
    Release("Release"),
}
