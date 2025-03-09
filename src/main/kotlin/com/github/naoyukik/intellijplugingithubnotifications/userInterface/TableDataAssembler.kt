package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.SubjectType
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.TableDataDto
import com.intellij.openapi.util.IconLoader
import java.net.URI
import java.net.URL
import javax.swing.Icon
import kotlin.collections.map

class TableDataAssembler {
    companion object {
        private val TYPE_TO_PATH = mapOf(
            "PullRequest" to "pull",
            "Issue" to "issues",
            "Release" to "releases",
        )

        private val issuesOpened = IconLoader.getIcon(
            "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-opened-16.svg",
            this::class.java.classLoader,
        )

        private val issuesClosed = IconLoader.getIcon(
            "com/github/naoyukik/intellijplugingithubnotifications/icons/issue-closed-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsOpen = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsMerged = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-merge-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsClosed = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-closed-16.svg",
            this::class.java.classLoader,
        )

        private val pullRequestsDraft = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/git-pull-request-draft-16.svg",
            this::class.java.classLoader,
        )

        private val release = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/tag-16.svg",
            this::class.java.classLoader,
        )

        private val unread = IconLoader.getIcon(
            "/com/github/naoyukik/intellijplugingithubnotifications/icons/unread-16.svg",
            this::class.java.classLoader,
        )

        private val TYPE_TO_EMOJI: Map<String, Icon> = mapOf(
            "PullRequestOpen" to pullRequestsOpen,
            "PullRequestMerged" to pullRequestsMerged,
            "PullRequestClosed" to pullRequestsClosed,
            "PullRequestDraft" to pullRequestsDraft,
            "IssueOpen" to issuesOpened,
            "IssueClosed" to issuesClosed,
            "Release" to release,
            "Unread" to unread,
        )
    }

    fun toTableDataDto(list: List<GitHubNotificationDto>): List<TableDataDto> {
        return list.map {
            val issueNumber = getIssueNumber(it.subject.url)
            TableDataDto(
                title = it.subject.title,
                fullName = apiUrlToRepositoryIssueNumberConverter(
                    repositoryFullName = it.repository.fullName,
                    issueNumber = issueNumber,
                    type = it.subject.type,
                ),
                htmlUrl = apiUrlToHtmlUrlConverter(
                    htmlUrl = it.repository.htmlUrl,
                    issueNumber = issueNumber,
                    type = it.subject.type,
                ),
                reason = it.reason,
                updatedAt = it.updatedAt,
                typeEmoji = apiUrlToEmojiConverter(it),
                reviewers = it.detail?.requestedReviewers?.map { reviewer -> reviewer.login } ?: emptyList(),
                unreadEmoji = it.unread.takeIf { it }?.let { TYPE_TO_EMOJI["Unread"] },
            )
        }
    }

    private fun apiUrlToRepositoryIssueNumberConverter(
        repositoryFullName: String,
        issueNumber: String,
        type: SubjectType,
    ): String {
        return TYPE_TO_PATH[type.name]?.let { typeToPath ->
            when (typeToPath) {
                "issues", "pull" -> "$repositoryFullName #$issueNumber"
                "releases" -> "$issueNumber in $repositoryFullName"
                else -> "$repositoryFullName #$issueNumber"
            }
        } ?: "$repositoryFullName #$issueNumber"
    }

    private fun apiUrlToHtmlUrlConverter(htmlUrl: String, issueNumber: String, type: SubjectType): URL? {
        return TYPE_TO_PATH[type.name]?.let { typeToPath ->
            when (typeToPath) {
                "issues", "pull" -> URI("$htmlUrl/$typeToPath/$issueNumber").toURL()
                "releases" -> URI("$htmlUrl/$typeToPath/tag/$issueNumber").toURL()
                else -> null
            }
        }
    }

    @Suppress("ComplexMethod")
    private fun apiUrlToEmojiConverter(notification: GitHubNotificationDto): Icon? {
        return when (val type = notification.subject.type) {
            SubjectType.PullRequest -> notification.let {
                return when {
                    it.isPullRequestDraft() -> TYPE_TO_EMOJI["PullRequestDraft"]
                    it.isPullRequestClosed() -> TYPE_TO_EMOJI["PullRequestClosed"]
                    it.isPullRequestMerged() -> TYPE_TO_EMOJI["PullRequestMerged"]
                    it.isPullRequestOpen() -> TYPE_TO_EMOJI["PullRequestOpen"]
                    else -> null
                }
            }
            SubjectType.Issue -> notification.let {
                return when {
                    it.isIssueClosed() -> TYPE_TO_EMOJI["IssueClosed"]
                    it.isIssueOpen() -> TYPE_TO_EMOJI["IssueOpen"]
                    else -> null
                }
            }
            SubjectType.Release -> TYPE_TO_EMOJI[type.name]
            SubjectType.UNKNOWN -> null
        }
    }

    private fun getIssueNumber(url: String): String = url.split("/").last()
}
