package com.github.naoyukik.intellijplugingithubnotifications.utility

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeParseException

/**
 * Unit tests for DateTimeHandler class using Kotest.
 *
 * The convertToLocalDateTime method is tested to ensure it correctly converts
 * a valid UTC date string into a local date-time string in the format "yyyy-MM-dd HH:mm:ss".
 */
class DateTimeHandlerTest : StringSpec({

    "convertToLocalDateTime should correctly format UTC date string to local date-time" {
        // Arrange
        val utcDate = "2024-12-29T10:00:45Z"
        val currentZoneId = ZoneId.systemDefault()
        val expectedLocalDateTime = Instant.parse(utcDate).atZone(currentZoneId)
            .toLocalDateTime()
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        // Act
        val result = DateTimeHandler.convertToLocalDateTime(utcDate)

        // Assert
        result shouldBe expectedLocalDateTime
    }

    "convertToLocalDateTime should throw exception for invalid date string" {
        // Arrange
        val invalidDate = "invalid-date-string"

        // Act & Assert
        shouldThrow<DateTimeParseException> {
            DateTimeHandler.convertToLocalDateTime(invalidDate)
        }
    }

    "convertToLocalDateTime should maintain second-level precision during conversion" {
        // Arrange
        val utcDate = "2024-12-29T10:00:45Z"
        val specificZoneId = ZoneId.of("Asia/Tokyo") // +0900

        // Act
        val result = DateTimeHandler.convertToLocalDateTime(utcDate, specificZoneId)

        // Assert
        result shouldBe "2024-12-29 19:00:45"
    }
})
