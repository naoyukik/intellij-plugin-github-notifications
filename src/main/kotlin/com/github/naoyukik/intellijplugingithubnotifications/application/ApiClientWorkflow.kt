package com.github.naoyukik.intellijplugingithubnotifications.application

import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto
import com.github.naoyukik.intellijplugingithubnotifications.domain.GitHubNotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.SettingStateRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification.SubjectType
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetail
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.NotificationDetailResponse.NotificationDetailError
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.SettingState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.time.ZonedDateTime
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.Repository as DtoRepository
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.Subject as DtoSubject
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.GitHubNotificationDto.SubjectType as DtoSubjectType
import com.github.naoyukik.intellijplugingithubnotifications.application.dto.NotificationDetailDto.NotificationDetail.RequestedReviewers as DtoRequestedReviewers

class ApiClientWorkflow(
    private val repository: GitHubNotificationRepository,
    private val settingStateRepository: SettingStateRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private var latestFetchTime: ZonedDateTime? = null

    suspend fun fetchNotifications(): List<GitHubNotificationDto> = withContext(dispatcher) {
        val settingState = settingStateRepository.loadSettingState()
        val ghCliPath = settingState.ghCliPath
        val includingRead = settingState.includingRead
        val resultLimit = settingState.resultLimit
        if (!hasNewNotificationsSinceLastCheck(settingState)) return@withContext emptyList()

        val notifications = settingState.repositoryName.takeUnless { it.isEmpty() }?.let { nonEmptyRepositoryName ->
            repository.fetchNotificationsByRepository(ghCliPath, nonEmptyRepositoryName, includingRead, resultLimit)
        } ?: repository.fetchNotifications(ghCliPath, includingRead, resultLimit)

        notifications.takeIf { it.isNotEmpty() } ?: return@withContext emptyList()

        val updatedNotifications = notifications.map { notification ->
            val detailAPiPath = setDetailApiPath(notification)
            val updatedNotification = detailAPiPath?.let {
                val detail = repository.fetchNotificationsReleaseDetail(
                    ghCliPath = ghCliPath,
                    repositoryName = notification.repository.fullName,
                    detailApiPath = it,
                )
                when (detail) {
                    is NotificationDetail -> {
                        notification.copy(
                            subject = notification.subject.copy(
                                url = detail.htmlUrl,
                            ),
                            detail = detail,
                        )
                    }
                    is NotificationDetailError -> null
                }
            } ?: notification
            updatedNotification
        }

        updatedNotifications.toGitHubNotificationDto()
    }

    private fun hasNewNotificationsSinceLastCheck(
        settingState: SettingState,
    ): Boolean {
        val previousFetchTime = latestFetchTime ?: run {
            latestFetchTime = ZonedDateTime.now()
            return true
        }
        latestFetchTime = ZonedDateTime.now()
        val ghCliPath = settingState.ghCliPath
        val includingRead = settingState.includingRead
        val latestNotifications = settingState.repositoryName.takeUnless { repositoryName ->
            repositoryName.isEmpty()
        }?.let { nonEmptyRepositoryName ->
            repository.fetchLatestNotificationsByRepository(
                ghCliPath = ghCliPath,
                repositoryName = nonEmptyRepositoryName,
                previousTime = previousFetchTime,
                includingRead = includingRead,
            )
        } ?: repository.fetchLatestNotifications(
            ghCliPath = ghCliPath,
            previousTime = previousFetchTime,
            includingRead = includingRead,
        )
        return latestNotifications.isNotEmpty()
    }

    private fun setDetailApiPath(notification: GitHubNotification): String? {
        return when (val type = notification.subject.type) {
            SubjectType.Release, SubjectType.Issue, SubjectType.PullRequest ->
                "${type.setApiPath()}/${setDetailId(notification)}}"
            SubjectType.UNKNOWN -> null
        }
    }

    private fun setDetailId(notification: GitHubNotification): String? {
        return when (notification.subject.type) {
            SubjectType.Release, SubjectType.Issue, SubjectType.PullRequest -> {
                val detailId = URI(
                    notification.subject.url,
                ).path.substringAfterLast("/")
                detailId.ifEmpty { null }
            }
            SubjectType.UNKNOWN -> null
        }
    }

    private fun List<GitHubNotification>.toGitHubNotificationDto(): List<GitHubNotificationDto> {
        return this.map {
            GitHubNotificationDto(
                id = it.id,
                reason = it.reason,
                updatedAt = it.updatedAt,
                unread = it.unread,
                subject = DtoSubject(
                    title = it.subject.title,
                    url = it.subject.url,
                    type = DtoSubjectType.valueOf(
                        it.subject.type.name,
                    ),
                ),
                repository = DtoRepository(
                    fullName = it.repository.fullName,
                    htmlUrl = it.repository.htmlUrl,
                ),
                detail = NotificationDetailDto.NotificationDetail(
                    state = it.detail?.state.toString(),
                    merged = it.detail?.merged == true,
                    draft = it.detail?.draft == true,
                    htmlUrl = it.detail?.htmlUrl.toString(),
                    requestedReviewers = it.detail?.requestedReviewers?.map { reviewer ->
                        DtoRequestedReviewers(login = reviewer.login)
                    } ?: emptyList(),
                ),
            )
        }
    }
}
