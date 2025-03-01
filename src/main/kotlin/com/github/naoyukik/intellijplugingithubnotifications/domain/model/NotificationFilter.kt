package com.github.naoyukik.intellijplugingithubnotifications.domain.model

data class NotificationFilter(
    val types: Set<String> = emptySet(),
)
