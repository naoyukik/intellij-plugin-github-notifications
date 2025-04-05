package com.github.naoyukik.intellijplugingithubnotifications.domain.model

data class SettingState(
    val fetchInterval: Int,
    val repositoryName: String,
    val ghCliPath: String,
    val includingRead: Boolean,
    val resultLimit: Int,
    val forceRefresh: Boolean,
)
