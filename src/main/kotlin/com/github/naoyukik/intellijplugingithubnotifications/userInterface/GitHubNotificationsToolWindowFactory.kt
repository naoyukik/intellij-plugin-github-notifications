package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.github.naoyukik.intellijplugingithubnotifications.applicaton.ApiClientWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.applicaton.NotificationWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.applicaton.SettingStateWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.applicaton.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.infrastructure.GitHubNotificationRepositoryImpl
import com.github.naoyukik.intellijplugingithubnotifications.infrastructure.SettingStateRepositoryImpl
import com.github.naoyukik.intellijplugingithubnotifications.utility.DateTimeHandler
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.util.Timer
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableColumn
import kotlin.concurrent.fixedRateTimer
import kotlin.coroutines.CoroutineContext

@Suppress("TooManyFunctions")
class GitHubNotificationsToolWindowFactory : ToolWindowFactory, DumbAware, CoroutineScope, Disposable {
    private val apiClientWorkflow = ApiClientWorkflow(GitHubNotificationRepositoryImpl())
    private val settingStateWorkflow = SettingStateWorkflow(SettingStateRepositoryImpl())
    private val coroutineJob = Job()
    private var timer: Timer? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val notificationToolTable = initializeJBTable()
        setupMouseListener(notificationToolTable)

        val actionGroup = DefaultActionGroup()

        val refreshAction = createRefreshAction(notificationToolTable)

        actionGroup.add(refreshAction)

        val notificationToolPanel = notificationToolTable.toJBScrollPane().toJBPanel()
        val actionToolbar = createActionToolbar(actionGroup, notificationToolPanel)
        notificationToolPanel.add(actionToolbar.component, BorderLayout.WEST)

        val content = ContentFactory.getInstance().createContent(notificationToolPanel, null, false)
        toolWindow.contentManager.addContent(content)

        refreshNotifications(notificationToolTable)

        Disposer.register(toolWindow.disposable, this)

        val settingState = settingStateWorkflow.getFetchInterval()

        startAutoRefresh(notificationToolTable, settingState.fetchInterval, project)
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
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val notifications = apiClientWorkflow.fetchNotifications()
                        table.model = notifications.toJBTable().model
                        setColumnWidth(table, 0, setCalculateLinkColumnWidth(table))
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    } catch (ex: IllegalStateException) {
                        ex.printStackTrace()
                    } catch (ex: IllegalArgumentException) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }

    private fun refreshNotifications(table: JBTable) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val notifications = apiClientWorkflow.fetchNotifications()
                table.model = notifications.toJBTable().model
                setColumnWidth(table, 0, setCalculateLinkColumnWidth(table))
            } catch (ex: IOException) {
                ex.printStackTrace()
            } catch (ex: IllegalArgumentException) {
                ex.printStackTrace()
            }
        }
    }

    private fun initializeJBTable(): JBTable {
        val columnName = arrayOf(
            "Link",
            "Message",
            "Reason",
            "Updated at",
        )
        return JBTable(object : DefaultTableModel(arrayOf(), columnName) {
            override fun isCellEditable(row: Int, column: Int) = false
        }).apply {
            setColumnWidth(this, 0, setCalculateLinkColumnWidth(this))
        }
    }

    private fun setColumnWidth(table: JBTable, columnIndex: Int, width: Int): TableColumn? {
        return table.columnModel.getColumn(
            columnIndex,
        ).apply {
            this.preferredWidth = width
            this.maxWidth = width
            this.minWidth = width
        }
    }

    private fun setupMouseListener(table: JBTable) {
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val row = table.rowAtPoint(e.point)
                val col = table.columnAtPoint(e.point)

                // 対象のセルがリンク列である場合
                if (col == 0) {
                    val link = table.getValueAt(row, col).toString()
                    val url = Regex("href='([^']*)'").find(link)?.groupValues?.get(1) ?: ""
                    if (url.isEmpty()) return
                    try {
                        val desktop = Desktop.getDesktop()
                        desktop.browse(URI(url))
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    } catch (ex: URISyntaxException) {
                        ex.printStackTrace()
                    }
                }
            }
        })
    }

    private fun startAutoRefresh(table: JBTable, minute: Int, project: Project) {
        timer = fixedRateTimer(
            name = "GitHubNotificationRefresher",
            initialDelay = 0L,
            period = (minute * 60 * 1000).toLong(),
        ) {
            refreshNotifications(table)
            NotificationWorkflow().fetchedNotification(project)
        }
    }

    private fun stopAutoRefresh() {
        timer?.cancel()
        timer = null
    }

    override fun dispose() {
        stopAutoRefresh()
        coroutineJob.cancel()
    }

    private fun List<TableDataDto>.toJBTable(): JBTable {
        val columnName = arrayOf(
            "Link",
            "Message",
            "Reason",
            "Updated at",
        )
        val data = this.map { dto ->
            val updatedAt = DateTimeHandler.convertToLocalDateTime(dto.updatedAt)
            val htmlUrl = dto.htmlUrl?.let { "<html><a href='$it'>Open</a></html>" } ?: ""
            arrayOf(
                htmlUrl,
                "<html>${dto.fullName}<br>${dto.title}</html>",
                "<html>${dto.reason}</html>",
                "<html>$updatedAt</html>",
            )
        }.toTypedArray()

        val tableModel = object : DefaultTableModel(data, columnName) {
            override fun isCellEditable(row: Int, column: Int) = false
        }
        val table = JBTable(tableModel).apply {
            setColumnWidth(this, 0, setCalculateLinkColumnWidth(this))
        }

        return table
    }

    private fun setCalculateColumnWidth(table: JBTable, text: String, padding: Int = 10): Int {
        val fontWidth = table.getFontMetrics(table.font).run {
            this.stringWidth(text)
        }
        return fontWidth + padding
    }

    private fun setCalculateLinkColumnWidth(table: JBTable): Int {
        return setCalculateColumnWidth(table, "Open")
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
