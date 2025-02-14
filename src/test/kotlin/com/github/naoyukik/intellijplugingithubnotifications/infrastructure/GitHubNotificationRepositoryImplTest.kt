package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse
import com.github.naoyukik.intellijplugingithubnotifications.utility.CommandExecutor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject

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

        mockkObject(CommandExecutor)
        every { CommandExecutor.execute(any()) } returns sampleJson

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotifications(ghCliPath)

        result.size shouldBe 1
        result[0].id shouldBe "1"
        result[0].subject.title shouldBe "Test Notification"
    }

    "fetchNotifications should return an empty list when no data is returned" {
        val ghCliPath = "testPath"

        mockkObject(CommandExecutor)
        every { CommandExecutor.execute(listOf(ghCliPath, "api", "/notifications")) } returns null

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotifications(ghCliPath)

        result.size shouldBe 0
    }

    "fetchNotifications should throw an exception when invalid JSON is returned" {
        val ghCliPath = "testPath"
        val invalidJson = "invalid json"

        mockkObject(CommandExecutor)
        every { CommandExecutor.execute(listOf(ghCliPath, "api", "/notifications")) } returns invalidJson

        val repository = GitHubNotificationRepositoryImpl()

        shouldThrow<Exception> {
            repository.fetchNotifications(ghCliPath)
        }
    }

    "fetchNotificationsReleaseDetail should return a valid NotificationDetailResponse when valid data is returned" {
        val ghCliPath = "testPath"
        val repositoryName = "test/repo"
        val detailApiPath = "issues/1"

        val validJson = """
            * Request at 2025-02-13 21:58:31.8861502 +0900 JST m=+0.118526501
            * Request to https://api.github.com/notifications
            > GET /notifications HTTP/1.1

            < HTTP/2.0 200 OK

            {
                "id": "1",
                "state": "open",
                "merged": false,
                "draft": false,
                "html_url": "https://github.com/test/repo/issues/1",
                "requested_reviewers": []
            }
        """.trimIndent()

        mockkObject(CommandExecutor)
        every {
            CommandExecutor.execute(
                listOf(
                    ghCliPath,
                    "api",
                    "/repos/$repositoryName/$detailApiPath",
                    "--verbose",
                ),
            )
        } returns validJson

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotificationsReleaseDetail(
            ghCliPath = ghCliPath,
            repositoryName = repositoryName,
            detailApiPath = detailApiPath,
        )

        result shouldBe NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://github.com/test/repo/issues/1",
            requestedReviewers = emptyList(),
        )
    }

    "fetchNotificationsReleaseDetail should return a valid NotificationDetailError when Accessed unauthorized data" {
        val ghCliPath = "testPath"
        val repositoryName = "test/repo"
        val detailApiPath = "issues/1"

        val validJson = """
            * Request at 2025-02-13 21:58:31.8861502 +0900 JST m=+0.118526501
            * Request to https://api.github.com/notifications
            > GET /notifications HTTP/1.1

            < HTTP/2.0 200 OK

            {
              "message": "Not Found",
              "documentation_url": "https://docs.github.com/rest/releases/releases#get-a-release",
              "status": "404"
            }
        """.trimIndent()

        mockkObject(CommandExecutor)
        every {
            CommandExecutor.execute(
                listOf(
                    ghCliPath,
                    "api",
                    "/repos/$repositoryName/$detailApiPath",
                    "--verbose",
                ),
            )
        } returns validJson

        val repository = GitHubNotificationRepositoryImpl()
        val result = repository.fetchNotificationsReleaseDetail(
            ghCliPath = ghCliPath,
            repositoryName = repositoryName,
            detailApiPath = detailApiPath,
        )

        result shouldBe NotificationDetailResponse.NotificationDetailError(
            message = "Not Found",
            documentationUrl = "https://docs.github.com/rest/releases/releases#get-a-release",
            status = "404",
        )
    }

    "fetchNotificationsReleaseDetail should throw an exception when invalid JSON is returned" {
        val ghCliPath = "testPath"
        val repositoryName = "test/repo"
        val detailApiPath = "issues/1"
        val invalidJson = "invalid json"

        mockkObject(CommandExecutor)
        every {
            CommandExecutor.execute(
                listOf(
                    ghCliPath,
                    "api",
                    "/repos/$repositoryName/$detailApiPath",
                    "--verbose",
                ),
            )
        } returns invalidJson

        val repository = GitHubNotificationRepositoryImpl()

        shouldThrow<Exception> {
            repository.fetchNotificationsReleaseDetail(
                ghCliPath = ghCliPath,
                repositoryName = repositoryName,
                detailApiPath = detailApiPath,
            )
        }
    }

    "fetchNotificationsReleaseDetail should throw an exception when no data is returned" {
        val ghCliPath = "testPath"
        val repositoryName = "test/repo"
        val detailApiPath = "issues/1"

        mockkObject(CommandExecutor)
        every {
            CommandExecutor.execute(
                listOf(
                    ghCliPath,
                    "api",
                    "/repos/$repositoryName/$detailApiPath",
                    "--verbose",
                ),
            )
        } returns null

        val repository = GitHubNotificationRepositoryImpl()

        shouldThrow<IllegalArgumentException> {
            repository.fetchNotificationsReleaseDetail(
                ghCliPath = ghCliPath,
                repositoryName = repositoryName,
                detailApiPath = detailApiPath,
            )
        }
    }
})
