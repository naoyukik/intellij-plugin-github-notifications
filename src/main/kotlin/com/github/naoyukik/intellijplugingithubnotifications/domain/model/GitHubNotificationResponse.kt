package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Serializable
sealed class GitHubNotificationResponse {

    @Serializable
    @SerialName("detail")
    data class GitHubNotification(
        val id: String,
        val subject: Subject,
        val repository: Repository,
        val reason: String,
        @SerialName("updated_at")
        val updatedAt: String,
        val detail: NotificationDetail? = null,
    ) : GitHubNotificationResponse() {
        @Serializable
        data class Subject(
            val title: String,
            val url: String,
            val type: SubjectType,
        )

        @Serializable
        data class Repository(
            @SerialName("full_name")
            val fullName: String,
            @SerialName("html_url")
            val htmlUrl: String,
        )

        @Serializable
        enum class SubjectType {
            Issue,
            PullRequest,
            Release,
            UNKNOWN,
            ;

            fun isRelevantType(): Boolean {
                return when (this) {
                    Issue, PullRequest, Release -> true
                    UNKNOWN -> false
                }
            }

            fun setApiPath(): String? {
                return when (this) {
                    Issue -> "issues"
                    PullRequest -> "pulls"
                    Release -> "releases"
                    UNKNOWN -> null
                }
            }
        }
    }

    @Serializable
    @SerialName("error")
    data class GitHubNotificationError(
        val message: String,
        val status: String,
        @SerialName("documentation_url")
        val documentationUrl: String,
    ) : GitHubNotificationResponse()

    fun deserializeNotificationResponse(jsonString: String): GitHubNotificationResponse {
        val module = SerializersModule {
            polymorphic(GitHubNotificationResponse::class) {
                subclass(GitHubNotification::class)
                subclass(GitHubNotificationError::class)
            }
        }

        val json = Json {
            serializersModule = module
            ignoreUnknownKeys = true
            classDiscriminator = "type"
        }

        return json.decodeFromString(serializer(), jsonString)
    }
}
