package com.github.naoyukik.intellijplugingithubnotifications.application.dto

import java.net.URL
import javax.swing.Icon

data class TableDataDto(
    val title: String,
    val fullName: String,
    val htmlUrl: URL?,
    val reason: String,
    val updatedAt: String,
    val typeEmoji: Icon?,
)
