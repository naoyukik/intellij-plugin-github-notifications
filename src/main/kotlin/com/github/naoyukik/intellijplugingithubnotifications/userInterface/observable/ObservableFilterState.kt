package com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable

import com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter.NotificationFilter
import kotlin.properties.Delegates

class ObservableFilterState(initialFilter: NotificationFilter) {

    private val listeners = mutableListOf<(NotificationFilter) -> Unit>()

    var filter: NotificationFilter by Delegates.observable(initialFilter) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            listeners.forEach { it(newValue) }
        }
    }

    fun addListener(listener: (NotificationFilter) -> Unit) {
        listeners.add(listener)
    }
}
