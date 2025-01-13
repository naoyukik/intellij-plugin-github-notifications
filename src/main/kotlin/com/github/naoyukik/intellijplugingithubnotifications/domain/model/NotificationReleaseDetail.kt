package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationReleaseDetail(
    @SerialName("html_url")
    val htmlUrl: String,
)
