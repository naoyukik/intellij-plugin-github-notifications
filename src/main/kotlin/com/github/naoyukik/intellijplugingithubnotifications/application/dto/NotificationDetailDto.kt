package com.github.naoyukik.intellijplugingithubnotifications.application.dto

sealed class NotificationDetailDto {
    data class NotificationDetail(
        val state: String = "",
        val merged: Boolean = false,
        val draft: Boolean = false,
        val htmlUrl: String,
        val requestedReviewers: List<RequestedReviewers> = emptyList(),
    ) : NotificationDetailDto() {
        data class RequestedReviewers(
            val login: String,
        )

        enum class State {
            Open,
            Closed,
        }
    }
}
