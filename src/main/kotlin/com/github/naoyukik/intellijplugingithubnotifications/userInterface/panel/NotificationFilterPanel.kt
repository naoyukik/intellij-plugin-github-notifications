package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationFilter
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationType
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComboBox

class NotificationFilterPanel(private val filterState: ObservableFilterState) {

    private var selectedType: String? = null
    private lateinit var typeComboBox: JComboBox<String>

    fun create(): DialogPanel {
        val dialogPanel = panel {
            group("Filters:") {
                row("Type") {
                    typeComboBox = comboBox(
                        NotificationType.entries.map { it.displayName },
                    ).bindItem(
                        getter = { filterState.filter.type },
                        setter = { newType -> filterState.filter = NotificationFilter(type = newType) },
                    ).applyToComponent {
                        addActionListener {
                            selectedType = this.selectedItem as? String
                            println("タイプ選択が変更されました: $selectedType")
                            filterState.filter = NotificationFilter(
                                type = selectedType,
                            )
                        }
                    }.component
                }
            }
        }

        return dialogPanel
    }
}
