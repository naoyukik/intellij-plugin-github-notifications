package com.github.naoyukik.intellijplugingithubnotifications.services

import com.github.naoyukik.intellijplugingithubnotifications.MyBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class MyProjectService(project: Project) {
    companion object {
        private const val RANDOM_RANGE_START = 1
        private const val RANDOM_RANGE_END = 100
    }

    init {
        thisLogger().info(MyBundle.message("projectService", project.name))
        thisLogger().warn(
            "Don't forget to remove all non-needed sample code files with" +
                " their corresponding registration entries in `plugin.xml`."
        )
    }

    fun getRandomNumber() = (RANDOM_RANGE_START..RANDOM_RANGE_END).random()
}
