package com.github.naoyukik.intellijplugingithubnotifications.application

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitRepositoryDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitProviderService

class GitProviderWorkflow(
    private val gitProviderService: GitProviderService,
) {
    fun getGitRepositories(): List<GitRepositoryDto> {
        return gitProviderService.getGitRepositories().map {
            GitRepositoryDto(it.name)
        }
    }
}
