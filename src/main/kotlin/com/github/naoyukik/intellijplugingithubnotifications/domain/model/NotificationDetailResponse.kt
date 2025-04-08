package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
sealed class NotificationDetailResponse {
    @Serializable
    data class NotificationDetail(
        val state: String = "",
        val merged: Boolean = false,
        val draft: Boolean = false,
        @SerialName("html_url")
        val htmlUrl: String,
        @SerialName("requested_reviewers")
        val requestedReviewers: List<RequestedReviewers> = emptyList(),
        @SerialName("requested_teams")
        val requestedTeams: List<RequestedTeams> = emptyList(),
        val labels: List<Label> = emptyList(),
    ) : NotificationDetailResponse() {
        @Serializable
        data class RequestedReviewers(
            val login: String,
        )

        @Serializable
        data class RequestedTeams(
            val name: String,
        )

        @Serializable
        data class Label(
            val name: String,
        )

        fun isPullRequestOpen(): Boolean {
            return this.state == "open"
        }

        fun isPullRequestMerged(): Boolean {
            return this.merged
        }

        fun isPullRequestClosed(): Boolean {
            return this.state == "closed" && !this.merged
        }

        fun isPullRequestDraft(): Boolean {
            return this.draft
        }

        fun isIssueOpen(): Boolean {
            return this.state == IssueState.Open.name.lowercase()
        }

        fun isIssueClosed(): Boolean {
            return this.state == IssueState.Closed.name.lowercase()
        }

        enum class IssueState {
            Open,
            Closed,
        }
    }

    @Serializable
    @SerialName("error")
    data class NotificationDetailError(
        val message: String,
        val status: String,
        @SerialName("documentation_url")
        val documentationUrl: String,
    ) : NotificationDetailResponse()

    object NotificationDetailResponseSerializer : JsonContentPolymorphicSerializer<NotificationDetailResponse>(
        NotificationDetailResponse::class,
    ) {
        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<NotificationDetailResponse> {
            val jsonObj = element.jsonObject

            if (jsonObj.containsKey("id")) {
                return NotificationDetail.serializer()
            }

            if (
                jsonObj.containsKey("message") &&
                jsonObj.containsKey("status") &&
                jsonObj.containsKey("documentation_url")
            ) {
                return NotificationDetailError.serializer()
            }

            throw IllegalArgumentException("Unknown GitHub Notification Detail Response")
        }
    }
}
