package com.github.naoyukik.intellijplugingithubnotifications.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class AppSettingsConfigurable : Configurable {
    private var mySettingsComponent: AppSettingsComponent? = null
    private var mySettingsState: AppSettingsState? = null

    init {
        mySettingsComponent = AppSettingsComponent()
        mySettingsState = AppSettingsState.getInstance()
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String {
        return "GitHub Notifications"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = AppSettingsComponent()
        return mySettingsComponent?.getPanel()
    }

    override fun isModified(): Boolean {
        return mySettingsComponent?.getCustomizedFetchInterval() != mySettingsState?.myState?.fetchInterval
    }

    override fun apply() {
        mySettingsState?.myState?.fetchInterval = mySettingsComponent!!.getCustomizedFetchInterval()
    }

    override fun reset() {
        mySettingsComponent?.setCustomizedFetchInterval(mySettingsState?.myState?.fetchInterval)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
