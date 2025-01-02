package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.SettingStateDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository

class SettingStateWorkflow(
    private val repository: SettingStateRepository,
) {
    fun getFetchInterval(): SettingStateDto {
        val settingState = repository.getFetchInterval()
        return SettingStateDto(
            fetchInterval = settingState.fetchInterval,
        )
    }
}
