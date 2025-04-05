package com.github.naoyukik.intellijplugingithubnotifications.application.dto

data class SettingStateDto(
    val fetchInterval: Int,
    val repositoryName: String,
    val forceRefresh: Boolean,
)
