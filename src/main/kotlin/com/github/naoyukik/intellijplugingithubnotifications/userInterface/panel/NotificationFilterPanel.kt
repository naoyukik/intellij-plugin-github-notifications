package com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.toMutableProperty
import com.intellij.util.ui.JBUI
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.SwingUtilities

class NotificationFilterPanel(private val filterState: ObservableFilterState) {

    private var selectedType: String? = null
    private var selectedUnread: String? = null
    private var selectedLabel: String? = DEFAULT_LABEL
    private var selectedReviewer: String? = DEFAULT_REVIEWER
    private var notificationLabels = DefaultComboBoxModel(arrayOf(DEFAULT_LABEL))
    private var notificationReviewer = DefaultComboBoxModel(arrayOf(DEFAULT_REVIEWER))

    companion object {
        const val PADDING_LEFT = 38
        const val DEFAULT_LABEL = "<Choose Label>"
        const val DEFAULT_REVIEWER = "<Choose Reviewer>"
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
                comboBox(notificationReviewer).bindItem(
                    ::selectedReviewer.toMutableProperty(),
                ).applyToComponent {
                    addActionListener {
                        selectedReviewer = this.selectedItem as? String
                        filterState.filter = filterState.filter.copy(reviewer = selectedReviewer)
                    }
                }
                comboBox(notificationLabels).bindItem(
                    ::selectedLabel.toMutableProperty(),
                ).applyToComponent {
                    isEditable = true
                    setupPlaceholder(this, DEFAULT_LABEL)
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
        val currentSelectedItem = selectedLabel ?: DEFAULT_LABEL
        val newItems = currentNotifications.flatMap { notification: GitHubNotificationDto ->
            notification.detail?.labels?.map { label -> label.name } ?: emptyList()
        }.distinct().sortedBy { it.lowercase() }

        updateComboBoxItem(
            defaultComboBox = notificationLabels,
            newItems = newItems,
            currentSelectedItem = currentSelectedItem,
            defaultItem = DEFAULT_LABEL,
        )
    }

    fun notificationReviewersAndTeamsToComboBoxItems(currentNotifications: List<GitHubNotificationDto>) {
        val currentSelectedItem = selectedReviewer ?: DEFAULT_REVIEWER
        val newReviewers = currentNotifications.flatMap { notification: GitHubNotificationDto ->
            notification.detail?.requestedReviewers?.map { reviewer -> reviewer.login } ?: emptyList()
        }
        val newTeams = currentNotifications.flatMap { notification ->
            notification.detail?.requestedTeams?.map { team -> team.name } ?: emptyList()
        }
        val newItems = (newReviewers + newTeams).distinct().sortedBy { it.lowercase() }

        updateComboBoxItem(
            defaultComboBox = notificationReviewer,
            newItems = newItems,
            currentSelectedItem = currentSelectedItem,
            defaultItem = DEFAULT_REVIEWER,
        )
    }

    private fun updateComboBoxItem(
        defaultComboBox: DefaultComboBoxModel<String>,
        newItems: List<String>,
        currentSelectedItem: String,
        defaultItem: String,
    ) {
        SwingUtilities.invokeLater {
            defaultComboBox.removeAllElements()
            defaultComboBox.addElement(defaultItem)
            newItems.forEach { defaultComboBox.addElement(it) }
            val existsItem = newItems.contains(currentSelectedItem).let { exists ->
                if (exists) currentSelectedItem else defaultItem
            }
            defaultComboBox.selectedItem = existsItem
        }
    }

    private fun setupPlaceholder(comboBox: JComboBox<String>, placeholder: String) {
        if (!comboBox.isEditable) {
            comboBox.isEditable = true
        }

        val editor = comboBox.editor.editorComponent as javax.swing.JTextField
        if (comboBox.selectedItem == null || comboBox.selectedItem!!.toString().isEmpty()) {
            editor.text = placeholder
        }

        editor.addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent?) {
                if (editor.text == placeholder) {
                    editor.text = ""
                }
            }

            override fun focusLost(e: FocusEvent?) {
                if (editor.text.isEmpty()) {
                    editor.text = placeholder
                }
            }
        })
    }
}
