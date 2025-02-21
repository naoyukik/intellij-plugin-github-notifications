package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState
import com.github.naoyukik.intellijplugingithubnotifications.settings.AppSettingsState
import com.intellij.openapi.project.Project

class SettingStateRepositoryImpl(project: Project) : SettingStateRepository {
    private val state = AppSettingsState.getInstance(project)

    companion object {
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15
        private const val RESULT_LIMIT_DEFAULT_VALUE = 30
    }

    override fun loadSettingState(): SettingState {
        val fetchInterval = state?.myState?.fetchInterval ?: FETCH_INTERVAL_DEFAULT_VALUE
        val repositoryName = state?.myState?.repositoryName ?: ""
        val ghCliPath = state?.myState?.ghCliPath?.ifEmpty { "gh" } ?: "gh"
        val includingRead = state?.myState?.includingRead == true
        val resultLimit = state?.myState?.resultLimit ?: RESULT_LIMIT_DEFAULT_VALUE
        return SettingState(
            fetchInterval = fetchInterval,
            repositoryName = repositoryName,
            ghCliPath = ghCliPath,
            includingRead = includingRead,
            resultLimit = resultLimit,
        )
    }
}
