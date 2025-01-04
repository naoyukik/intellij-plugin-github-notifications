package com.github.naoyukik.intellijplugingithubnotifications.applicaton

import com.github.naoyukik.intellijplugingithubnotifications.domain.NotificationService
import com.intellij.openapi.project.Project

class NotificationWorkflow {
    fun fetchedNotification(project: Project) {
        NotificationService().fetchedNotification(project, "GitHub Notifications has been updated.")
    }

    fun fetchedNotificationForError(project: Project, message: String) {
        NotificationService().fetchedNotificationForError(project, message)
    }
}
