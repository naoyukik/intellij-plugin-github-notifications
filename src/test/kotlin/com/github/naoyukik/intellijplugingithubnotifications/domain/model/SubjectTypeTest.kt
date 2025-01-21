package com.github.naoyukik.intellijplugingithubnotifications.domain.model

import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

/**
 * Test class for the SubjectType enumeration.
 *
 * The `isRelevantType` method is tested to check whether the given
 * SubjectType is considered relevant or not, which is currently expected
 * to always return true for valid SubjectType values.
 */
class SubjectTypeTest : StringSpec({
    "isRelevantType returns true for Issue" {
        // Arrange
        val subjectType = GitHubNotification.SubjectType.Issue
        // Assert
        assertTrue(subjectType.isRelevantType())
    }

    "isRelevantType returns true for Release" {
        // Arrange
        val subjectType = GitHubNotification.SubjectType.Release
        // Assert
        assertTrue(subjectType.isRelevantType())
    }

    "isRelevantType returns true for PullRequest" {
        // Arrange
        val subjectType = GitHubNotification.SubjectType.PullRequest
        // Assert
        assertTrue(subjectType.isRelevantType())
    }

    "isRelevantType returns true for UNKNOWN" {
        // Arrange
        val subjectType = GitHubNotification.SubjectType.UNKNOWN
        // Assert
        assertFalse(subjectType.isRelevantType())
    }
})
