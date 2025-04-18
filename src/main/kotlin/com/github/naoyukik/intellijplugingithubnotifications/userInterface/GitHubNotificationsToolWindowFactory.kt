package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.github.naoyukik.intellijplugingithubnotifications.application.ApiClientWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.application.NotificationWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.application.SettingStateWorkflow
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.infrastructure.GitHubNotificationRepositoryImpl
import com.github.naoyukik.intellijplugingithubnotifications.infrastructure.SettingStateRepositoryImpl
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.dto.TableDataDto
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.filter.NotificationFilter
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.observable.ObservableFilterState
import com.github.naoyukik.intellijplugingithubnotifications.userInterface.panel.NotificationFilterPanel
import com.github.naoyukik.intellijplugingithubnotifications.utility.DateTimeHandler
import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import com.intellij.util.IconUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableColumn
import kotlin.coroutines.CoroutineContext

@Service(Service.Level.PROJECT)
private class ProjectData : CoroutineScope, Disposable {
    lateinit var apiClientWorkflow: ApiClientWorkflow
    lateinit var settingStateWorkflow: SettingStateWorkflow
    lateinit var tableDataAssembler: TableDataAssembler
    lateinit var notificationFilterPanel: NotificationFilterPanel
    val coroutineJob = Job()
    var currentNotifications: List<GitHubNotificationDto> = emptyList()
    var filteredNotifications: List<GitHubNotificationDto> = emptyList()
    val filterState = ObservableFilterState(
        NotificationFilter(
            unread = null,
            type = null,
            reviewer = null,
            label = null,
        ),
    )

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun dispose() {
        coroutineJob.cancel()
    }
}

@Suppress("TooManyFunctions")
class GitHubNotificationsToolWindowFactory : ToolWindowFactory, DumbAware {
    val columnName = arrayOf(
        "Link",
        "Unread",
        "Type",
        "Message",
        "Reason",
        "Reviewers",
        "Labels",
        "Updated at",
    )

    companion object {
        const val ICON_WIDTH = 13
        const val ICON_HEIGHT = 13
        const val COLUMN_NUMBER_LINK = 0
        const val COLUMN_NUMBER_UNREAD = 1
        const val COLUMN_NUMBER_TYPE = 2
        const val ROW_MIN_HEIGHT = 37
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val projectData = project.getService(ProjectData::class.java)
        projectData.apiClientWorkflow = ApiClientWorkflow(
            GitHubNotificationRepositoryImpl(),
            SettingStateRepositoryImpl(project),
        )
        projectData.settingStateWorkflow = SettingStateWorkflow(SettingStateRepositoryImpl(project))
        val settingState = projectData.settingStateWorkflow.loadSettingState()
        projectData.tableDataAssembler = TableDataAssembler()

        val notificationToolTable = initializeJBTable()
        setupMouseListener(notificationToolTable)

        val actionGroup = DefaultActionGroup()
        val refreshAction = createRefreshAction(notificationToolTable, project, settingState.forceRefresh)
        actionGroup.add(refreshAction)

        val notificationToolPanel = notificationToolTable.toJBScrollPane().toJBPanel()
        val actionToolbar = createActionToolbar(actionGroup, notificationToolPanel)
        notificationToolPanel.add(actionToolbar.component, BorderLayout.WEST)

        // filter panel
        projectData.notificationFilterPanel = NotificationFilterPanel(projectData.filterState)
        projectData.filterState.addListener {
            projectData.filteredNotifications = NotificationFilter.applyFilter(projectData.currentNotifications, it)
            val displayTable = projectData.tableDataAssembler.toTableDataDto(projectData.filteredNotifications)
            fireTableDataChanged(notificationToolTable, displayTable)
        }
        val filterPanel = projectData.notificationFilterPanel.create()
        notificationToolPanel.add(filterPanel, BorderLayout.NORTH)

        val content = ContentFactory.getInstance().createContent(notificationToolPanel, null, false)
        toolWindow.contentManager.addContent(content)

        Disposer.register(toolWindow.disposable) {
            projectData.dispose()
        }

        startAutoRefresh(notificationToolTable, settingState.fetchInterval, project)
    }

    private fun fireTableDataChanged(
        notificationToolTable: JBTable,
        displayTable: List<TableDataDto>,
    ) {
        notificationToolTable.autoCreateColumnsFromModel = false
        notificationToolTable.model = displayTable.toJBTable().model
        setColumnWidth(
            notificationToolTable,
            COLUMN_NUMBER_LINK,
            setCalculateLinkColumnWidth(notificationToolTable),
        )
        setColumnWidth(
            notificationToolTable,
            COLUMN_NUMBER_UNREAD,
            setCalculateUnreadColumnWidth(notificationToolTable),
        )
        setColumnWidth(
            notificationToolTable,
            COLUMN_NUMBER_TYPE,
            setCalculateTypeColumnWidth(notificationToolTable),
        )
        notificationToolTable.columnModel.getColumn(COLUMN_NUMBER_TYPE).cellRenderer =
            object : DefaultTableCellRenderer() {
                override fun setValue(value: Any?) {
                    if (value is Icon) {
                        this.icon = IconUtil.toSize(value, ICON_WIDTH, ICON_HEIGHT)
                        this.horizontalAlignment = CENTER
                        this.verticalAlignment = CENTER
                    } else {
                        this.icon = null
                    }
                }
            }

        notificationToolTable.columnModel.getColumn(COLUMN_NUMBER_UNREAD).cellRenderer =
            object : DefaultTableCellRenderer() {
                override fun setValue(value: Any?) {
                    if (value is Icon) {
                        this.icon = IconUtil.toSize(value, ICON_WIDTH, ICON_HEIGHT)
                        this.horizontalAlignment = CENTER
                        this.verticalAlignment = CENTER
                    } else {
                        this.icon = null
                    }
                }
            }
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

    private fun createRefreshAction(table: JBTable, project: Project, isForceRefresh: Boolean): AnAction {
        return object : AnAction(
            "Refresh Notifications",
            "Fetch latest GitHub notifications",
            AllIcons.General.InlineRefresh,
        ) {
            override fun actionPerformed(e: AnActionEvent) {
                refreshNotifications(table, project, isForceRefresh)
            }
        }
    }

    private fun refreshNotifications(table: JBTable, project: Project, isForceRefresh: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val data = project.getService(ProjectData::class.java)
                val notifications = data.apiClientWorkflow.fetchNotifications(isForceRefresh)
                notifications.isEmpty() && return@launch
                data.currentNotifications = notifications
                data.notificationFilterPanel.notificationLabelsToComboBoxItems(data.currentNotifications)
                data.notificationFilterPanel.notificationReviewersAndTeamsToComboBoxItems(data.currentNotifications)
                data.filteredNotifications = NotificationFilter.applyFilter(
                    data.currentNotifications,
                    data.filterState.filter,
                )
                val displayTable = data.tableDataAssembler.toTableDataDto(data.filteredNotifications)
                fireTableDataChanged(table, displayTable)

                NotificationWorkflow().fetchedNotification(project)
                NotificationManager(project).updateBadge()
            } catch (e: IllegalArgumentException) {
                NotificationWorkflow().fetchedNotificationForError(project, e.message ?: "Unknown error")
            }
        }
    }

    private fun initializeJBTable(): JBTable {
        return JBTable(object : DefaultTableModel(arrayOf(), columnName) {
            override fun isCellEditable(row: Int, column: Int) = false
        }).apply {
            rowHeight = ROW_MIN_HEIGHT
            setColumnWidth(this, COLUMN_NUMBER_LINK, setCalculateLinkColumnWidth(this))
            setColumnWidth(this, COLUMN_NUMBER_UNREAD, setCalculateUnreadColumnWidth(this))
            setColumnWidth(this, COLUMN_NUMBER_TYPE, setCalculateTypeColumnWidth(this))
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
                if (row < 0 || col < 0) return

                if (col == COLUMN_NUMBER_LINK) {
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
        val data = project.getService(ProjectData::class.java)

        data.launch {
            while (isActive) {
                refreshNotifications(table, project, false)
                delay(timeMillis = minute * 60 * 1000L)
            }
        }
    }

    private fun List<TableDataDto>.toJBTable(): JBTable {
        val data = this.map { dto ->
            val updatedAt = DateTimeHandler.convertToLocalDateTime(dto.updatedAt)
            val htmlUrl = dto.htmlUrl?.let { "<html><a href='$it'>Open</a></html>" } ?: ""
            arrayOf(
                htmlUrl,
                dto.unreadEmoji ?: "",
                dto.typeEmoji ?: "",
                "<html>${dto.fullName}<br>${dto.title}</html>",
                "<html>${dto.reason}</html>",
                "<html>${dto.reviewers.joinToString(", ")}</html>",
                "<html>${dto.labels.joinToString(", ")}</html>",
                "<html>$updatedAt</html>",
            )
        }.toTypedArray()

        val tableModel = object : DefaultTableModel(data, columnName) {
            override fun isCellEditable(row: Int, column: Int) = false
        }
        val table = JBTable(tableModel)

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

    private fun setCalculateTypeColumnWidth(table: JBTable): Int {
        return setCalculateColumnWidth(table, "Type")
    }

    private fun setCalculateUnreadColumnWidth(table: JBTable): Int {
        return setCalculateColumnWidth(table, "Unread")
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
