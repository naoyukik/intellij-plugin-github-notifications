package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState

interface SettingStateRepository {
    fun getFetchInterval(): SettingState
}
