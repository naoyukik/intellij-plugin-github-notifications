package com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationFilter
import kotlin.properties.Delegates

class ObservableFilterState(initialFilter: NotificationFilter) {

    // 登録されたリスナーを格納するリスト
    private val listeners = mutableListOf<(NotificationFilter) -> Unit>()

    // フィルターの状態を保持
    var filter: NotificationFilter by Delegates.observable(initialFilter) {
            _, oldValue, newValue ->
        println("Observable：フィルターが変更されました。oldValue: $oldValue, newValue: $newValue")
        if (oldValue != newValue) {
            listeners.forEach { it(newValue) }
        }
    }

    fun addListener(listener: (NotificationFilter) -> Unit) {
        println("add listener")
        listeners.add(listener)
    }

    fun removeListener(listener: (NotificationFilter) -> Unit) {
        listeners.remove(listener)
    }
}
