package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState
import com.github.naoyukik.intellijplugingithubnotifications.settings.AppSettingsState

class SettingStateRepositoryImpl : SettingStateRepository {
    private val state = AppSettingsState.getInstance()

    companion object {
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15
    }

    override fun getFetchInterval(): SettingState {
        val fetchInterval = state?.myState?.fetchInterval ?: FETCH_INTERVAL_DEFAULT_VALUE
        return SettingState(
            fetchInterval = fetchInterval,
        )
    }
}
