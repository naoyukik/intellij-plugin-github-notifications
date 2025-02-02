package com.github.naoyukik.intellijplugingithubnotifications.utility

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeHandler {
    fun convertToLocalDateTime(utcDate: String, zoneId: ZoneId = ZoneId.systemDefault()): String {
        val instant = Instant.parse(utcDate)
        val localDateTime = ZonedDateTime.ofInstant(instant, zoneId)
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime)
    }

    fun toIso8601(zonedDateTime: ZonedDateTime): String {
        val formatter = DateTimeFormatter.ISO_INSTANT
        return formatter.format(zonedDateTime)
    }
}
