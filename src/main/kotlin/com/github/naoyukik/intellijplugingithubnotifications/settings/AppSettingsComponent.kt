package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class AppSettingsComponent {
    companion object {
        private const val PADDING = 5
        private const val FETCH_INTERVAL_DEFAULT_VALUE = 15
        private const val FETCH_INTERVAL_MIN_VALUE = 1
        private const val FETCH_INTERVAL_MAX_VALUE = 60
    }

    private var mainPanel: JPanel? = null
    private val customizedFetchInterval: JBIntSpinner =
        JBIntSpinner(FETCH_INTERVAL_DEFAULT_VALUE, FETCH_INTERVAL_MIN_VALUE, FETCH_INTERVAL_MAX_VALUE, 1)

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Fetch interval:"),
                JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.X_AXIS)
                    add(customizedFetchInterval)
                    add(Box.createHorizontalStrut(PADDING))
                    @Suppress("DialogTitleCapitalization")
                    add(JBLabel("minutes"))
                },
                1, false,
            )
            .addComponent(
                JBLabel(
                    "Min: 1, Max: 60, A restart is required when the value is changed.",
                ),
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel? {
        return mainPanel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return customizedFetchInterval
    }

    fun getCustomizedFetchInterval(): Int {
        return customizedFetchInterval.value as Int
    }

    fun setCustomizedFetchInterval(newInt: Int?) {
        customizedFetchInterval.value = newInt
    }
}
