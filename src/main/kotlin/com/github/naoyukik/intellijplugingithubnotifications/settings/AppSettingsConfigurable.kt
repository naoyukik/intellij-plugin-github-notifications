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
        return isModifiedFetchInterval() || isModifiedRepositoryName() || isModifiedGhCliPath()
    }

    override fun apply() {
        mySettingsState?.myState?.fetchInterval = mySettingsComponent!!.getCustomizedFetchInterval()
        mySettingsState?.myState?.repositoryName = mySettingsComponent!!.getCustomizedRepositoryName()
        mySettingsState?.myState?.ghCliPath = mySettingsComponent!!.getCustomizedGhCliPath()
    }

    override fun reset() {
        mySettingsComponent?.setCustomizedFetchInterval(mySettingsState?.myState?.fetchInterval)
        mySettingsComponent?.setCustomizedRepositoryName(mySettingsState?.myState?.repositoryName)
        mySettingsComponent?.setCustomizedGhCliPath(mySettingsState?.myState?.ghCliPath)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }

    private fun isModifiedFetchInterval(): Boolean {
        return mySettingsComponent?.getCustomizedFetchInterval() != mySettingsState?.myState?.fetchInterval
    }

    private fun isModifiedRepositoryName(): Boolean {
        return mySettingsComponent?.getCustomizedRepositoryName() != mySettingsState?.myState?.repositoryName
    }

    private fun isModifiedGhCliPath(): Boolean {
        return mySettingsComponent?.getCustomizedGhCliPath() != mySettingsState?.myState?.ghCliPath
    }
}
