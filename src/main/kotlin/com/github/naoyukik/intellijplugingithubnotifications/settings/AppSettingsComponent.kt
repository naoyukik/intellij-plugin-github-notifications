package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.ui.JBIntSpinner
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
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
        private const val TEXT_AREA_COLUMNS = 30
    }

    private var mainPanel: JPanel? = null
    private val customizedFetchInterval: JBIntSpinner =
        JBIntSpinner(FETCH_INTERVAL_DEFAULT_VALUE, FETCH_INTERVAL_MIN_VALUE, FETCH_INTERVAL_MAX_VALUE, 1)
    private val customizedRepositoryName = JBTextField(TEXT_AREA_COLUMNS)
    private val customizedGhCliPath = JBTextField(TEXT_AREA_COLUMNS)
    private val customizedIncludingRead = JBCheckBox()

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
            .addLabeledComponent(
                JBLabel("Repository: "),
                customizedRepositoryName,
            )
            .addComponent(
                JBLabel(
                    "Repository format: OWNER/REPOSITORY_NAME",
                ),
            )
            .addComponent(
                JBLabel(
                    "If blank, retrieve all notifications.",
                ),
            )
            .addLabeledComponent(
                JBLabel("GH CLI path: "),
                customizedGhCliPath,
            )
            .addComponent(
                JBLabel(
                    "If it's not in your PATH, you can specify the GH CLI Path.",
                ),
            )
            .addComponent(
                JBLabel(
                    "By default, the \"gh\" command is used.",
                ),
            )
            .addLabeledComponent(
                JBLabel("Including read: "),
                customizedIncludingRead,
            )
            .addComponent(
                JBLabel(
                    "When turned on, read data is also acquired.",
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

    fun setCustomizedFetchInterval(value: Int?) {
        customizedFetchInterval.value = value
    }

    fun getCustomizedRepositoryName(): String {
        return customizedRepositoryName.text
    }

    fun setCustomizedRepositoryName(value: String?) {
        customizedRepositoryName.text = value
    }

    fun getCustomizedGhCliPath(): String {
        return customizedGhCliPath.text
    }

    fun setCustomizedGhCliPath(value: String?) {
        customizedGhCliPath.text = value
    }

    fun getCustomizedIncludingRead(): Boolean {
        return customizedIncludingRead.isSelected
    }

    fun setCustomizedIncludingRead(value: Boolean) {
        customizedIncludingRead.setSelected(value)
    }
}
