package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState
import com.github.naoyukik.intellijplugingithubnotifications.settings.AppSettingsState
import com.intellij.openapi.project.Project

class SettingStateRepositoryImpl(project: Project) : SettingStateRepository {
    private val state = AppSettingsState.getInstance(project)

    override fun loadSettingState(): SettingState {
        val fetchInterval = state.myState.fetchInterval
        val repositoryName = state.myState.repositoryName
        val ghCliPath = state.myState.ghCliPath.ifEmpty { "gh" }
        val includingRead = state.myState.includingRead
        val resultLimit = state.myState.resultLimit
        return SettingState(
            fetchInterval = fetchInterval,
            repositoryName = repositoryName,
            ghCliPath = ghCliPath,
            includingRead = includingRead,
            resultLimit = resultLimit,
        )
    }
}
