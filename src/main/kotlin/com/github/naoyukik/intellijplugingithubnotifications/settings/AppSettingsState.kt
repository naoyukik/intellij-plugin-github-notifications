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

    var customizedRepositoryName
        get() = myState.repositoryName
        set(value) {
            myState.repositoryName = value
        }

    var customizedFetchInterval
        get() = myState.fetchInterval
        set(value) {
            myState.fetchInterval = value
        }

    var customizedGhCliPath
        get() = myState.ghCliPath
        set(value) {
            myState.ghCliPath = value
        }
    var customizedIncludingRead
        get() = myState.includingRead
        set(value) {
            myState.includingRead = value
        }

    var customizedForceRefresh
        get() = myState.forceRefresh
        set(value) {
            myState.forceRefresh = value
        }

    var customizedResultLimit
        get() = myState.resultLimit
        set(value) {
            myState.resultLimit = value
        }

    class State {
        var fetchInterval = FETCH_INTERVAL_DEFAULT_VALUE
        var repositoryName: String = ""
        var ghCliPath: String = ""
        var includingRead = false
        var forceRefresh = false
        var resultLimit: Int = RESULT_LIMIT_DEFAULT_VALUE
    }

    companion object {
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15
        private const val RESULT_LIMIT_DEFAULT_VALUE = 30

        @JvmStatic
        fun getInstance(project: Project): AppSettingsState {
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
        myState.forceRefresh = state.forceRefresh
        myState.resultLimit = state.resultLimit
    }
}
