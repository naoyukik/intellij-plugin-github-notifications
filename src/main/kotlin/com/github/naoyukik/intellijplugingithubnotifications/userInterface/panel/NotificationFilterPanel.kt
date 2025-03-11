package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComboBox

class NotificationFilterPanel(private val filterState: ObservableFilterState) {

    private var selectedType: String? = null
    private lateinit var typeComboBox: JComboBox<String>

    fun create(): DialogPanel {
        val dialogPanel = panel {
            group("Filters") {
                row {
                    label("Type")
                    typeComboBox = comboBox(
                        NotificationType.entries.map { it.displayName },
                    ).bindItem(
                        getter = { filterState.filter.type },
                        setter = { newType -> filterState.filter = filterState.filter.copy(type = newType) },
                    ).applyToComponent {
                        addActionListener {
                            selectedType = this.selectedItem as? String
                            filterState.filter = filterState.filter.copy(
                                type = selectedType,
                            )
                        }
                    }.component
                    textField().bindText(
                        getter = { filterState.filter.reviewer ?: "" },
                        setter = { newReviewer ->
                            filterState.filter = filterState.filter.copy(reviewer = newReviewer)
                        },
                    ).applyToComponent {
                        emptyText.text = "Enter Reviewer"
                        addActionListener { filterState.filter = filterState.filter.copy(reviewer = this.text) }
                    }
                }
            }
        }

        return dialogPanel
    }
}
