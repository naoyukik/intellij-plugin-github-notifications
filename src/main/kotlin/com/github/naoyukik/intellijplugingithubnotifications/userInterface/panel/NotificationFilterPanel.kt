package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI

class NotificationFilterPanel(private val filterState: ObservableFilterState) {

    private var selectedType: String? = null
    private var selectedUnread: String? = null

    companion object {
        const val PADDING_LEFT = 38
    }

    fun create(): DialogPanel {
        val dialogPanel = panel {
            row {
                comboBox(NotificationUnread.entries.map { it.displayName }).bindItem(
                    getter = { filterState.filter.unread ?: NotificationUnread.ALL.displayName },
                    setter = { newUnread ->
                        filterState.filter = filterState.filter.copy(
                            unread = if (newUnread == NotificationUnread.ALL.displayName) null else newUnread,
                        )
                    },
                ).applyToComponent {
                    addActionListener {
                        selectedUnread = this.selectedItem as? String
                        filterState.filter = filterState.filter.copy(
                            unread = selectedUnread,
                        )
                    }
                }
                comboBox(
                    NotificationType.entries.map { it.displayName },
                ).bindItem(
                    getter = { filterState.filter.type ?: NotificationType.UNSELECTED.displayName },
                    setter = { newType ->
                        filterState.filter = filterState.filter.copy(
                            type = if (newType == NotificationType.UNSELECTED.displayName) null else newType,
                        )
                    },
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
                textField().bindText(
                    getter = { filterState.filter.label ?: "" },
                    setter = { newLabel ->
                        filterState.filter = filterState.filter.copy(label = newLabel)
                    },
                ).applyToComponent {
                    emptyText.text = "Enter Label"
                    addActionListener { filterState.filter = filterState.filter.copy(label = this.text) }
                }
            }
        }.apply {
            border = JBUI.Borders.emptyLeft(PADDING_LEFT)
        }

        return dialogPanel
    }
}
