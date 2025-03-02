package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationFilter
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationType
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel

class NotificationFilterPanel {

    private var selectedType: String? = null

    fun create(): DialogPanel = panel {
        group("Filters:") {
            row("Type") {
                comboBox(
                    NotificationType.entries.map { it.displayName },
                ).bindItem({ selectedType }, { newSelection ->
                    selectedType = newSelection
                    NotificationFilter(
                        type = selectedType,
                    )
                })
            }
        }
    }
}
