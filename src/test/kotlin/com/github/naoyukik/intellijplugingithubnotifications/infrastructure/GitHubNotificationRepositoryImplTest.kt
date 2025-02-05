package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.utility.CommandExecutor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GitHubNotificationRepositoryImplTest : StringSpec({

    "fetchNotifications should return a list of GitHub notifications when valid data is returned" {
        val ghCliPath = "testPath"
        val sampleJson = """
            [
                {
                    "id": "1",
                    "subject": {
                        "title": "Test Notification",
                        "url": "https://example.com/issue",
                        "type": "Issue"
                    },
                    "repository": {
                        "full_name": "sample/repo",
                        "html_url": "https://github.com/sample/repo"
                    },
                    "reason": "subscribed",
                    "updated_at": "2023-10-01T12:00:00Z"
                }
            ]
        """.trimIndent()

        val mockExecutor = mockk<CommandExecutor>()
        every { mockExecutor.execute(listOf(ghCliPath, "api", "/notifications")) } returns sampleJson

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotifications(ghCliPath)

        result.size shouldBe 1
        result[0].id shouldBe "1"
        result[0].subject.title shouldBe "Test Notification"
    }

    "fetchNotifications should return an empty list when no data is returned" {
        val ghCliPath = "testPath"

        val mockExecutor = mockk<CommandExecutor>()
        every { mockExecutor.execute(listOf(ghCliPath, "api", "/notifications")) } returns null

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotifications(ghCliPath)

        result.size shouldBe 0
    }

    "fetchNotifications should throw an exception when invalid JSON is returned" {
        val ghCliPath = "testPath"
        val invalidJson = "invalid json"

        val mockExecutor = mockk<CommandExecutor>()
        every { mockExecutor.execute(listOf(ghCliPath, "api", "/notifications")) } returns invalidJson

        val repository = GitHubNotificationRepositoryImpl()

        shouldThrow<Exception> {
            repository.fetchNotifications(ghCliPath)
        }
    }
})
