package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import org.jetbrains.annotations.Nls

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable(private val project: Project) : Configurable {
    private val mySettingsState
        get() = AppSettingsState.getInstance(project)

    private val mainPanel: DialogPanel by lazy { createUIComponents() }

    companion object {
        private const val FETCH_INTERVAL_MIN_VALUE = 1
        private const val FETCH_INTERVAL_MAX_VALUE = 60
        private const val COLUMNS_MEDIUM = 30
        private const val RESULT_LIMIT_MIN_VALUE = 1
        private const val RESULT_LIMIT_MAX_VALUE = 50
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "GitHub Notifications"
    }

    override fun createComponent(): DialogPanel {
        return mainPanel
    }

    override fun isModified(): Boolean {
        return mainPanel.isModified()
    }

    override fun reset() {
        mainPanel.reset()
    }

    override fun apply() {
        mainPanel.apply()
    }

    @Suppress("DialogTitleCapitalization")
    private fun createUIComponents(): DialogPanel {
        return panel {
            group("Notification Fetch Settings") {
                row("Fetch interval:") {
                    spinner(FETCH_INTERVAL_MIN_VALUE..FETCH_INTERVAL_MAX_VALUE)
                        .bindIntValue(mySettingsState::customizedFetchInterval)
                        .comment("Min: 1, Max: 60, A restart is required when the value is changed.")
                    label("minutes")
                }
                row("Result limit:") {
                    spinner(RESULT_LIMIT_MIN_VALUE..RESULT_LIMIT_MAX_VALUE)
                        .bindIntValue(mySettingsState::customizedResultLimit)
                        .comment("Min: 1, max: 50, Default: 30")
                }
                row {
                    checkBox("Show read notifications")
                        .bindSelected(mySettingsState::customizedIncludingRead)
                        .comment("When turned on, read data is also acquired.")
                }
            }
            group("GitHub Settings") {
                row("Repository:") {
                    textField()
                        .bindText(mySettingsState::customizedRepositoryName)
                        .columns(COLUMNS_MEDIUM)
                        .comment("Repository format: OWNER/REPOSITORY_NAME")
                }
                row("GH CLI path:") {
                    textField()
                        .bindText(mySettingsState::customizedGhCliPath)
                        .columns(COLUMNS_MEDIUM)
                        .comment("If it's not in your PATH, you can specify the GH CLI Path.")
                }
            }
        }
    }
}
