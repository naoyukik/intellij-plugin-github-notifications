package com.github.naoyukik.intellijplugingithubnotifications.domain

import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitRepository
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.ProjectLevelVcsManager

class GitProviderService(
    private val project: Project,
) {
    fun getGitRepositories(): List<GitRepository> {
        val vcsManager = ProjectLevelVcsManager.getInstance(project)
        val allVcsRoots = vcsManager.allVcsRoots
        val result = mutableListOf<GitRepository>()
        allVcsRoots.forEach {
            result.add(GitRepository(it.path.name))
        }
        return result
    }
}
