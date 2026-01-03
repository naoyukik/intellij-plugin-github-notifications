package com.github.naoyukik.intellijplugingithubnotifications.userInterface

import com.intellij.openapi.project.Project
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class GitHubNotificationsToolWindowFactoryTest : StringSpec({
    "isApplicableAsync should return true" {
        val factory = GitHubNotificationsToolWindowFactory()
        val project = mockk<Project>()
        factory.isApplicableAsync(project) shouldBe true
    }
})
