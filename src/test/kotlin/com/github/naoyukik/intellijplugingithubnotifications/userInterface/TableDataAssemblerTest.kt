package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.Subject
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.SubjectType
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto.NotificationDetail
import com.intellij.openapi.util.IconLoader
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.net.URI

class TableDataAssemblerTest : StringSpec({

    "should map a single GitHubNotificationDto to TableDataDto correctly" {
        val assembler = TableDataAssembler()

        val notificationDto = GitHubNotificationDto(
            id = "1",
            unread = true,
            subject = Subject(
                title = "Fix bug in login",
                url = "https://example.com/repos/owner/repo/issues/123",
                type = SubjectType.Issue,
            ),
            repository = GitHubNotificationDto.Repository(
                fullName = "owner/repo",
                htmlUrl = "https://example.com/owner/repo",
            ),
            reason = "subscribed",
            updatedAt = "2023-10-01T12:34:56Z",
            detail = mockk {
                every { state } returns NotificationDetail.State.Open.name.lowercase()
                every { merged } returns false
                every { draft } returns false
                every { htmlUrl } returns "https://example.com/"
                every { requestedReviewers } returns listOf(
                    mockk { every { login } returns "reviewer1" },
                    mockk { every { login } returns "reviewer2" },
                )
                every { requestedTeams } returns listOf(
                    mockk { every { name } returns "team1" },
                )
                every { labels } returns listOf(mockk { every { name } returns "bug" })
            },
        )

        val tableData = assembler.toTableDataDto(listOf(notificationDto))

        tableData.single().apply {
            title shouldBe "Fix bug in login"
            fullName shouldBe "owner/repo #123"
            htmlUrl shouldBe URI("https://example.com/owner/repo/issues/123").toURL()
            reason shouldBe "subscribed"
            updatedAt shouldBe "2023-10-01T12:34:56Z"
            typeEmoji shouldBe IconLoader.getIcon(
                "/com/github/naoyukik/intellijplugingithubnotifications/icons/issue-opened-16.svg",
                this::class.java.classLoader,
            )
            reviewers shouldBe listOf("reviewer1", "reviewer2", "team1")
            unreadEmoji shouldBe IconLoader.getIcon(
                "/com/github/naoyukik/intellijplugingithubnotifications/icons/unread-16.svg",
                this::class.java.classLoader,
            )
            labels shouldBe listOf("bug")
        }
    }
})
