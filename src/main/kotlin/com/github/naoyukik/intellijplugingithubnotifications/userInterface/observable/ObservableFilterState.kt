package com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable

import com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter.NotificationFilter
import kotlin.properties.Delegates

class ObservableFilterState(initialFilter: NotificationFilter) {

    // 登録されたリスナーを格納するリスト
    private val listeners = mutableListOf<(NotificationFilter) -> Unit>()

    // フィルターの状態を保持
    var filter: NotificationFilter by Delegates.observable(initialFilter) {
            _, oldValue, newValue ->
        if (oldValue != newValue) {
            listeners.forEach { it(newValue) }
        }
    }

    fun addListener(listener: (NotificationFilter) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (NotificationFilter) -> Unit) {
        listeners.remove(listener)
    }
}
