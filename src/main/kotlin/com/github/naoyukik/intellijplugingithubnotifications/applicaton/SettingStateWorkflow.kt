package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.SettingStateDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository

class SettingStateWorkflow(
    private val repository: SettingStateRepository,
) {
    fun loadSettingState(): SettingStateDto {
        val settingState = repository.loadSettingState()
        return SettingStateDto(
            fetchInterval = settingState.fetchInterval,
            repositoryName = settingState.repositoryName,
        )
    }
}
