package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetail
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class GitHubNotification(
    val id: String,
    val unread: Boolean,
    val subject: Subject,
    val repository: Repository,
    val reason: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val detail: NotificationDetail? = null,
) {
    @Serializable
    data class Subject(
        val title: String,
        val url: String?,
        val type: SubjectType,
    )

    @Serializable
    data class Repository(
        @SerialName("full_name")
        val fullName: String,
        @SerialName("html_url")
        val htmlUrl: String,
    )

    @Serializable(SubjectTypeSerializer::class)
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

    object SubjectTypeSerializer : KSerializer<SubjectType> {
        override val descriptor = PrimitiveSerialDescriptor("SubjectType", PrimitiveKind.STRING)

        override fun serialize(
            encoder: Encoder,
            value: SubjectType,
        ) {
            encoder.encodeString(value.name)
        }

        override fun deserialize(decoder: Decoder): SubjectType {
            val value = decoder.decodeString()
            return SubjectType.entries.find { it.name == value } ?: SubjectType.UNKNOWN
        }
    }
}
