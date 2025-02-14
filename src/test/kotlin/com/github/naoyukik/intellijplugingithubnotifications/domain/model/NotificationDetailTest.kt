package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NotificationDetailTest : StringSpec({
    "isPullRequestOpen should return true when state is 'open'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestOpen() shouldBe true
    }

    "isPullRequestOpen should return false when state is not 'open'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestOpen() shouldBe false
    }

    "isPullRequestMerged should return true when merged is true" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = true,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestMerged() shouldBe true
    }

    "isPullRequestMerged should return false when merged is false" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestMerged() shouldBe false
    }

    "isPullRequestClosed should return true when state is 'closed' and merged is false" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestClosed() shouldBe true
    }

    "isPullRequestClosed should return false when state is not 'closed'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestClosed() shouldBe false
    }

    "isPullRequestDraft should return true when draft is true" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = true,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestDraft() shouldBe true
    }

    "isPullRequestDraft should return false when draft is false" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isPullRequestDraft() shouldBe false
    }

    "isIssueOpen should return true when state is 'open'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isIssueOpen() shouldBe true
    }

    "isIssueOpen should return false when state is not 'open'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isIssueOpen() shouldBe false
    }

    "isIssueClosed should return true when state is 'closed'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "closed",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isIssueClosed() shouldBe true
    }

    "isIssueClosed should return false when state is not 'closed'" {
        val notificationDetail = NotificationDetailResponse.NotificationDetail(
            state = "open",
            merged = false,
            draft = false,
            htmlUrl = "https://example.com",
            requestedReviewers = emptyList(),
        )
        notificationDetail.isIssueClosed() shouldBe false
    }
})
