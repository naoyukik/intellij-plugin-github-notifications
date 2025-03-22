package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.LayeredIcon
import java.awt.Component
import java.awt.Graphics
import javax.swing.Icon

class NotificationManager(private val project: Project) {
    private val toolWindowManager = ToolWindowManager.getInstance(project)
    private val toolWindow = toolWindowManager.getToolWindow(TOOL_WINDOW_ID)

    private var hasModifiedIcon = false

    companion object {
        private const val TOOL_WINDOW_ID = "GitHub Notifications"
    }

    init {
        toolWindowStateChanged()
    }

    fun updateBadge() {
        val icon = toolWindow?.icon ?: AllIcons.Toolbar.Unknown

        toolWindow?.setIcon(icon)

        val fixedModifiedIcon = createModifiedIcon(icon)

        val layeredIcon = LayeredIcon(2)
        layeredIcon.setIcon(icon, 0)

        layeredIcon.setIcon(fixedModifiedIcon, 1)
        toolWindow?.setIcon(layeredIcon)
        hasModifiedIcon = true
    }

    private fun createModifiedIcon(icon: Icon): Icon = object : Icon {
        val modifiedIcon = AllIcons.General.Modified

        @Suppress("MagicNumber")
        override fun paintIcon(c: Component?, g: Graphics?, x: Int, y: Int) {
            val xIcon = icon.iconHeight / 2 - 2 - x
            val yIcon = y - 3
            modifiedIcon.paintIcon(c, g, xIcon, yIcon)
        }

        override fun getIconWidth(): Int {
            return modifiedIcon.iconWidth
        }

        override fun getIconHeight(): Int {
            return modifiedIcon.iconHeight
        }
    }

    private fun toolWindowStateChanged() {
        project.messageBus.connect().subscribe(
            ToolWindowManagerListener.TOPIC,
            object : ToolWindowManagerListener {
                override fun stateChanged(toolWindowManager: ToolWindowManager) {
                    val activeToolWindow = toolWindowManager.activeToolWindowId

                    val shouldRestoreIcon = activeToolWindow == TOOL_WINDOW_ID && hasModifiedIcon
                    if (shouldRestoreIcon) {
                        toolWindow?.setIcon(AllIcons.Toolbar.Unknown)
                        hasModifiedIcon = false
                    }
                }
            },
        )
    }
}
