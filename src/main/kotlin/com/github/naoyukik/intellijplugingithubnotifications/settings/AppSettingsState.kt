package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service
@State(
    name = "GitHubNotificationsState",
    storages = [Storage("GitHubNotificationsState.xml")],
)
class AppSettingsState : PersistentStateComponent<AppSettingsState.State> {
    val myState = State()

    class State {
        var fetchInterval = FETCH_INTERVAL_DEFAULT_VALUE
    }

    companion object {
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15

        @JvmStatic
        fun getInstance(): AppSettingsState? {
            return com.intellij.openapi.application.ApplicationManager.getApplication().getService(
                AppSettingsState::class.java,
            )
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.fetchInterval = state.fetchInterval
    }
}
