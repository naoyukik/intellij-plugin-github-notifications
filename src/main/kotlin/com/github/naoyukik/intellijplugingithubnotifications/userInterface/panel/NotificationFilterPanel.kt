package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.util.ui.JBUI
import javax.swing.DefaultComboBoxModel
import javax.swing.SwingUtilities

class NotificationFilterPanel(private val filterState: ObservableFilterState) {

    private var selectedType: String? = null
    private var selectedUnread: String? = null
    private var selectedLabel: String? = DEFAULT_LABEL
    private var notificationLabels = DefaultComboBoxModel(arrayOf(DEFAULT_LABEL))

    companion object {
        const val PADDING_LEFT = 38
        const val DEFAULT_LABEL = "<Choose Label>"
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
                }
                textField().bindText(
                    getter = { filterState.filter.reviewer ?: "" },
                    setter = { newReviewer ->
                        filterState.filter = filterState.filter.copy(reviewer = newReviewer)
                    },
                ).applyToComponent {
                    emptyText.text = "Enter Reviewer"
                    addActionListener { filterState.filter = filterState.filter.copy(reviewer = this.text) }
                }
                comboBox(notificationLabels).bindItem(
                    ::selectedLabel.toMutableProperty(),
                ).applyToComponent {
                    addActionListener {
                        selectedLabel = this.selectedItem as? String
                        filterState.filter = filterState.filter.copy(label = selectedLabel)
                    }
                }
            }
        }.apply {
            border = JBUI.Borders.emptyLeft(PADDING_LEFT)
        }

        return dialogPanel
    }

    fun notificationLabelsToComboBoxItems(currentNotifications: List<GitHubNotificationDto>) {
        val currentSelectedLabel = selectedLabel ?: DEFAULT_LABEL
        val newLabelsItems = currentNotifications.flatMap { notification: GitHubNotificationDto ->
            notification.detail?.labels?.map { label -> label.name } ?: emptyList()
        }.distinct().sorted()

        SwingUtilities.invokeLater {
            notificationLabels.removeAllElements()
            notificationLabels.addElement(DEFAULT_LABEL)
            newLabelsItems.forEach { label -> notificationLabels.addElement(label) }
            val existsLabel = newLabelsItems.contains(currentSelectedLabel).let { exists ->
                if (exists) currentSelectedLabel else DEFAULT_LABEL
            }
            notificationLabels.selectedItem = existsLabel
        }
    }
}
