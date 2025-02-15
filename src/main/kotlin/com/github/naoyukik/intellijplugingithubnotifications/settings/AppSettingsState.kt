package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
@State(
    name = "com.github.naoyukik.intellijplugingithubnotifications",
    storages = [Storage("GitHubNotifications.xml")],
)
class AppSettingsState : PersistentStateComponent<AppSettingsState.State> {
    val myState = State()

    class State {
        var fetchInterval = FETCH_INTERVAL_DEFAULT_VALUE
        var repositoryName: String = ""
        var ghCliPath: String = ""
        var includingRead = false
    }

    companion object {
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15

        @JvmStatic
        fun getInstance(project: Project): AppSettingsState? {
            return project.getService(AppSettingsState::class.java)
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.fetchInterval = state.fetchInterval
        myState.repositoryName = state.repositoryName
        myState.ghCliPath = state.ghCliPath
        myState.includingRead = state.includingRead
    }
}
