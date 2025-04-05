package com.github.naoyukik.intellijplugingithubnotifications.application

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.SettingStateDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository

class SettingStateWorkflow(
    private val repository: SettingStateRepository,
) {
    fun loadSettingState(): SettingStateDto {
        val settingState = repository.loadSettingState()
        return SettingStateDto(
            fetchInterval = settingState.fetchInterval,
            repositoryName = settingState.repositoryName,
            forceRefresh = settingState.forceRefresh,
        )
    }
}
