package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.ApiClientWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.infrastructure.NotificationRepositoryImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class GitHubNotificationsToolWindowFactory : ToolWindowFactory, DumbAware {
    private val apiClientWorkflow = ApiClientWorkflow(NotificationRepositoryImpl())

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val notifications = apiClientWorkflow.fetchNotifications()

        val notificationToolTable = notifications.toJBTable()

        val actionGroup = DefaultActionGroup()

        val refreshAction = createRefreshAction(notificationToolTable)

        actionGroup.add(refreshAction)

        val notificationToolPanel = notificationToolTable.toJBScrollPane().toJBPanel()
        val actionToolbar = createActionToolbar(actionGroup, notificationToolPanel)
        notificationToolPanel.add(actionToolbar.component, BorderLayout.WEST)

        val content = ContentFactory.getInstance().createContent(notificationToolPanel, null, false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createActionToolbar(actionGroup: DefaultActionGroup, targetComponent: JComponent): ActionToolbar {
        return ActionManager.getInstance().createActionToolbar(
            "GitHubNotificationToolbar",
            actionGroup,
            false,
        ).apply {
            setTargetComponent(targetComponent)
        }
    }

    private fun createRefreshAction(table: JBTable): AnAction {
        return object : AnAction(
            "Refresh Notifications",
            "Fetch latest GitHub notifications",
            AllIcons.General.InlineRefresh,
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                val notifications = apiClientWorkflow.fetchNotifications()
                table.model = notifications.toJBTable().model
            }
        }
    }

    private fun List<TableDataDto>.toJBTable(): JBTable {
        val columnName = arrayOf(
            "message",
            "Link",
        )
        val data = this.map { dto ->
            arrayOf(
                dto.title,
                "<html><a href='${dto.htmlUrl}'>Open</a></html>",
            )
        }.toTypedArray()

        val tableModel = DefaultTableModel(data, columnName)
        val table = JBTable(tableModel)

        // セルのリンクをクリック可能にする
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val row = table.rowAtPoint(e.point)
                val col = table.columnAtPoint(e.point)

                // 対象のセルがリンク列である場合
                if (col == 1) {
                    val link = table.getValueAt(row, col).toString()
                    val url = Regex("href='([^']*)'").find(link)?.groupValues?.get(1) ?: ""
                    if (url.isEmpty()) return
                    try {
                        val desktop = Desktop.getDesktop()
                        desktop.browse(java.net.URI(url))
                    } catch (ex: java.io.IOException) {
                        ex.printStackTrace()
                    } catch (ex: java.net.URISyntaxException) {
                        ex.printStackTrace()
                    }
                }
            }
        })

        return table
    }

    private fun JBTable.toJBScrollPane(): JBScrollPane {
        return JBScrollPane(this)
    }

    private fun JBScrollPane.toJBPanel(): JBPanel<JBPanel<*>> {
        return JBPanel<JBPanel<*>>(BorderLayout()).apply {
            add(this@toJBPanel)
        }
    }
}
