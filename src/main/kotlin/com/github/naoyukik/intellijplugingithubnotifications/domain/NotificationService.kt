package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class NotificationService {
    fun fetchedNotification(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("GitHub Notifications")
            .createNotification(content, NotificationType.INFORMATION)
            .notify(project)
    }

    fun fetchedNotificationForError(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("GitHub Notifications")
            .createNotification(content, NotificationType.ERROR)
            .notify(project)
    }
}
