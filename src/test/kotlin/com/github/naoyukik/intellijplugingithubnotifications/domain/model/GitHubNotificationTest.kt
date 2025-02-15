package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import io.kotest.core.spec.style.StringSpec
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

/**
 * Tests for the `GitHubNotification` class.
 *
 * Description:
 * The `GitHubNotification` class is a data model representing a notification in GitHub.
 * It includes information such as the notification's `id`, `subject`, `repository`, `reason`,
 * and the `updated_at` timestamp. The nested `Subject` class contains the title, URL, and type
 * of the subject, while the `Repository` class holds repository details.
 */
class GitHubNotificationTest : StringSpec({
    "should correctly serialize to JSON" {
        // Arrange
        val notification = GitHubNotification(
            id = "1",
            subject = GitHubNotification.Subject(
                title = "Test issue",
                url = "https://github.com/test/repo/issues/1",
                type = GitHubNotification.SubjectType.Issue,
            ),
            repository = GitHubNotification.Repository(
                fullName = "test/repo",
                htmlUrl = "https://github.com/test/repo",
            ),
            reason = "subscribed",
            updatedAt = "2023-11-01T10:00:00Z",
            unread = false,
        )

        // Act
        val serializedJson = Json.encodeToString(notification)

        // Assert
        assertNotNull(serializedJson)
        assert(serializedJson.contains("\"id\":\"1\""))
        assert(serializedJson.contains("\"title\":\"Test issue\""))
        assert(serializedJson.contains("\"full_name\":\"test/repo\""))
    }

    "should correctly deserialize from JSON" {
        // Arrange
        val json = """
            {
                "id": "1",
                "subject": {
                    "title": "Test issue",
                    "url": "https://github.com/test/repo/issues/1",
                    "type": "Issue"
                },
                "repository": {
                    "full_name": "test/repo",
                    "html_url": "https://github.com/test/repo"
                },
                "reason": "subscribed",
                "updated_at": "2023-11-01T10:00:00Z",
                "unread": false
            }
        """.trimIndent()

        // Act
        val notification = Json.decodeFromString<GitHubNotification>(json)

        // Assert
        assertEquals("1", notification.id)
        assertEquals("Test issue", notification.subject.title)
        assertEquals("https://github.com/test/repo/issues/1", notification.subject.url)
        assertEquals(GitHubNotification.SubjectType.Issue, notification.subject.type)
        assertEquals("test/repo", notification.repository.fullName)
        assertEquals("https://github.com/test/repo", notification.repository.htmlUrl)
        assertEquals("subscribed", notification.reason)
        assertEquals("2023-11-01T10:00:00Z", notification.updatedAt)
        assertEquals(false, notification.unread)
    }

    "should correctly handle different SubjectType values" {
        // Arrange
        val subjectTypes = listOf(
            GitHubNotification.SubjectType.Issue,
            GitHubNotification.SubjectType.PullRequest,
            GitHubNotification.SubjectType.Release,
        )

        subjectTypes.forEach { type ->
            // Act
            val notification = GitHubNotification(
                id = "1",
                subject = GitHubNotification.Subject(
                    title = "Title for $type",
                    url = "https://github.com/test/repo/$type",
                    type = type,
                ),
                repository = GitHubNotification.Repository(
                    fullName = "test/repo",
                    htmlUrl = "https://github.com/test/repo",
                ),
                reason = "assigned",
                updatedAt = "2023-11-01T10:00:00Z",
                unread = false,
            )

            val serializedJson = Json.encodeToString(notification)
            val deserializedNotification = Json.decodeFromString<GitHubNotification>(serializedJson)

            // Assert
            assertEquals(notification, deserializedNotification)
        }
    }

    "should correctly serialize and deserialize Subject object" {
        // Arrange
        val subject = GitHubNotification.Subject(
            title = "Test Notification",
            url = "https://github.com/test/repo/notification",
            type = GitHubNotification.SubjectType.Release,
        )

        // Act
        val serializedJson = Json.encodeToString(subject)
        val deserializedSubject = Json.decodeFromString<GitHubNotification.Subject>(serializedJson)

        // Assert
        assertEquals(subject, deserializedSubject)
    }

    "should correctly serialize and deserialize Repository object" {
        // Arrange
        val repository = GitHubNotification.Repository(
            fullName = "test/repo",
            htmlUrl = "https://github.com/test/repo",
        )

        // Act
        val serializedJson = Json.encodeToString(repository)
        val deserializedRepository = Json.decodeFromString<GitHubNotification.Repository>(serializedJson)

        // Assert
        assertEquals(repository, deserializedRepository)
    }
})
