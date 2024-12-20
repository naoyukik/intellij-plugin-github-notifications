package com.github.naoyukik.intellijplugingithubnotifications.infrastructure

import com.github.naoyukik.intellijplugingithubnotifications.domain.NotificationRepository
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubNotification
import com.github.naoyukik.intellijplugingithubnotifications.domain.model.GitHubToken
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class NotificationRepositoryImpl : NotificationRepository {
    private val client = OkHttpClient()

    override fun fetchNotifications(token: GitHubToken): List<GitHubNotification> {
        val request = Request.Builder()
            .url("https://api.github.com/notifications")
            .header("Authorization", "Bearer ${token.value}")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body?.toGitHubNotification() ?: emptyList()
        }
    }

    override fun fetchNotificationsMock(): List<GitHubNotification> {
        val responseJson = """
        [
          {
            "id":"13731497750",
            "unread":true,
            "reason":"subscribed",
            "updated_at":"2024-12-07T13:11:50Z",
            "last_read_at":"2024-12-07T12:57:32Z",
            "subject":{
              "title":"Bump org.jetbrains.intellij.platform from 2.1.0 to 2.2.0",
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/pulls/7",
              "latest_comment_url":null,
              "type":"PullRequest"
            },
            "repository":{
              "id":886160895,
              "node_id":"R_kgDONNG9_w",
              "name":"intellij-plugin-tab-manager-by-git",
              "full_name":"naoyukik/intellij-plugin-tab-manager-by-git",
              "private":false,
              "owner":{
                "login":"naoyukik",
                "id":10615135,
                "node_id":"MDQ6VXNlcjEwNjE1MTM1",
                "avatar_url":"https://avatars.githubusercontent.com/u/10615135?v=4",
                "gravatar_id":"",
                "url":"https://api.github.com/users/naoyukik",
                "html_url":"https://github.com/naoyukik",
                "followers_url":"https://api.github.com/users/naoyukik/followers",
                "following_url":"https://api.github.com/users/naoyukik/following{/other_user}",
                "gists_url":"https://api.github.com/users/naoyukik/gists{/gist_id}",
                "starred_url":"https://api.github.com/users/naoyukik/starred{/owner}{/repo}",
                "subscriptions_url":"https://api.github.com/users/naoyukik/subscriptions",
                "organizations_url":"https://api.github.com/users/naoyukik/orgs",
                "repos_url":"https://api.github.com/users/naoyukik/repos",
                "events_url":"https://api.github.com/users/naoyukik/events{/privacy}",
                "received_events_url":"https://api.github.com/users/naoyukik/received_events",
                "type":"User",
                "user_view_type":"public",
                "site_admin":false
              },
              "html_url":"https://github.com/naoyukik/intellij-plugin-tab-manager-by-git",
              "description":"Open and close tabs according to Git branches",
              "fork":false,
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git",
              "forks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/forks",
              "keys_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/keys{/key_id}",
              "collaborators_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/collaborators{/collaborator}",
              "teams_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/teams",
              "hooks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/hooks",
              "issue_events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues/events{/number}",
              "events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/events",
              "assignees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/assignees{/user}",
              "branches_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/branches{/branch}",
              "tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/tags",
              "blobs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/blobs{/sha}",
              "git_tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/tags{/sha}",
              "git_refs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/refs{/sha}",
              "trees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/trees{/sha}",
              "statuses_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/statuses/{sha}",
              "languages_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/languages",
              "stargazers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/stargazers",
              "contributors_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/contributors",
              "subscribers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/subscribers",
              "subscription_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/subscription",
              "commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/commits{/sha}",
              "git_commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/commits{/sha}",
              "comments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/comments{/number}",
              "issue_comment_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues/comments{/number}",
              "contents_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/contents/{+path}",
              "compare_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/compare/{base}...{head}",
              "merges_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/merges",
              "archive_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/{archive_format}{/ref}",
              "downloads_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/downloads",
              "issues_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues{/number}",
              "pulls_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/pulls{/number}",
              "milestones_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/milestones{/number}",
              "notifications_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/notifications{?since,all,participating}",
              "labels_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/labels{/name}",
              "releases_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/releases{/id}",
              "deployments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/deployments"
            },
            "url":"https://api.github.com/notifications/threads/13731497750",
            "subscription_url":"https://api.github.com/notifications/threads/13731497750/subscription"
          },
          {
            "id":"13656393897",
            "unread":true,
            "reason":"subscribed",
            "updated_at":"2024-12-07T13:12:49Z",
            "last_read_at":null,
            "subject":{
              "title":"Bump org.gradle.toolchains.foojay-resolver-convention from 0.8.0 to 0.9.0",
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/pulls/6",
              "latest_comment_url":null,
              "type":"PullRequest"
            },
            "repository":{
              "id":886160895,
              "node_id":"R_kgDONNG9_w",
              "name":"intellij-plugin-tab-manager-by-git",
              "full_name":"naoyukik/intellij-plugin-tab-manager-by-git",
              "private":false,
              "owner":{
                "login":"naoyukik",
                "id":10615135,
                "node_id":"MDQ6VXNlcjEwNjE1MTM1",
                "avatar_url":"https://avatars.githubusercontent.com/u/10615135?v=4",
                "gravatar_id":"",
                "url":"https://api.github.com/users/naoyukik",
                "html_url":"https://github.com/naoyukik",
                "followers_url":"https://api.github.com/users/naoyukik/followers",
                "following_url":"https://api.github.com/users/naoyukik/following{/other_user}",
                "gists_url":"https://api.github.com/users/naoyukik/gists{/gist_id}",
                "starred_url":"https://api.github.com/users/naoyukik/starred{/owner}{/repo}",
                "subscriptions_url":"https://api.github.com/users/naoyukik/subscriptions",
                "organizations_url":"https://api.github.com/users/naoyukik/orgs",
                "repos_url":"https://api.github.com/users/naoyukik/repos",
                "events_url":"https://api.github.com/users/naoyukik/events{/privacy}",
                "received_events_url":"https://api.github.com/users/naoyukik/received_events",
                "type":"User",
                "user_view_type":"public",
                "site_admin":false
              },
              "html_url":"https://github.com/naoyukik/intellij-plugin-tab-manager-by-git",
              "description":"Open and close tabs according to Git branches",
              "fork":false,
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git",
              "forks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/forks",
              "keys_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/keys{/key_id}",
              "collaborators_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/collaborators{/collaborator}",
              "teams_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/teams",
              "hooks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/hooks",
              "issue_events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues/events{/number}",
              "events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/events",
              "assignees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/assignees{/user}",
              "branches_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/branches{/branch}",
              "tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/tags",
              "blobs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/blobs{/sha}",
              "git_tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/tags{/sha}",
              "git_refs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/refs{/sha}",
              "trees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/trees{/sha}",
              "statuses_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/statuses/{sha}",
              "languages_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/languages",
              "stargazers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/stargazers",
              "contributors_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/contributors",
              "subscribers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/subscribers",
              "subscription_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/subscription",
              "commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/commits{/sha}",
              "git_commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/git/commits{/sha}",
              "comments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/comments{/number}",
              "issue_comment_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues/comments{/number}",
              "contents_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/contents/{+path}",
              "compare_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/compare/{base}...{head}",
              "merges_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/merges",
              "archive_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/{archive_format}{/ref}",
              "downloads_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/downloads",
              "issues_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/issues{/number}",
              "pulls_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/pulls{/number}",
              "milestones_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/milestones{/number}",
              "notifications_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/notifications{?since,all,participating}",
              "labels_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/labels{/name}",
              "releases_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/releases{/id}",
              "deployments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-tab-manager-by-git/deployments"
            },
            "url":"https://api.github.com/notifications/threads/13656393897",
            "subscription_url":"https://api.github.com/notifications/threads/13656393897/subscription"
          },
          {
            "id":"13592645959",
            "unread":true,
            "reason":"subscribed",
            "updated_at":"2024-12-07T13:07:50Z",
            "last_read_at":null,
            "subject":{
              "title":"build(deps): Bump org.jetbrains.kotlin.jvm from 2.0.20 to 2.1.0",
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/pulls/61",
              "latest_comment_url":null,
              "type":"PullRequest"
            },
            "repository":{
              "id":777766611,
              "node_id":"R_kgDOLlvG0w",
              "name":"intellij-plugin-copy-pretty-git-log",
              "full_name":"naoyukik/intellij-plugin-copy-pretty-git-log",
              "private":false,
              "owner":{
                "login":"naoyukik",
                "id":10615135,
                "node_id":"MDQ6VXNlcjEwNjE1MTM1",
                "avatar_url":"https://avatars.githubusercontent.com/u/10615135?v=4",
                "gravatar_id":"",
                "url":"https://api.github.com/users/naoyukik",
                "html_url":"https://github.com/naoyukik",
                "followers_url":"https://api.github.com/users/naoyukik/followers",
                "following_url":"https://api.github.com/users/naoyukik/following{/other_user}",
                "gists_url":"https://api.github.com/users/naoyukik/gists{/gist_id}",
                "starred_url":"https://api.github.com/users/naoyukik/starred{/owner}{/repo}",
                "subscriptions_url":"https://api.github.com/users/naoyukik/subscriptions",
                "organizations_url":"https://api.github.com/users/naoyukik/orgs",
                "repos_url":"https://api.github.com/users/naoyukik/repos",
                "events_url":"https://api.github.com/users/naoyukik/events{/privacy}",
                "received_events_url":"https://api.github.com/users/naoyukik/received_events",
                "type":"User",
                "user_view_type":"public",
                "site_admin":false
              },
              "html_url":"https://github.com/naoyukik/intellij-plugin-copy-pretty-git-log",
              "description":"Effortlessly format and copy Git Logs with customizable options in IntelliJ Plugin",
              "fork":false,
              "url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log",
              "forks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/forks",
              "keys_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/keys{/key_id}",
              "collaborators_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/collaborators{/collaborator}",
              "teams_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/teams",
              "hooks_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/hooks",
              "issue_events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/issues/events{/number}",
              "events_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/events",
              "assignees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/assignees{/user}",
              "branches_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/branches{/branch}",
              "tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/tags",
              "blobs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/git/blobs{/sha}",
              "git_tags_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/git/tags{/sha}",
              "git_refs_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/git/refs{/sha}",
              "trees_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/git/trees{/sha}",
              "statuses_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/statuses/{sha}",
              "languages_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/languages",
              "stargazers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/stargazers",
              "contributors_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/contributors",
              "subscribers_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/subscribers",
              "subscription_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/subscription",
              "commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/commits{/sha}",
              "git_commits_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/git/commits{/sha}",
              "comments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/comments{/number}",
              "issue_comment_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/issues/comments{/number}",
              "contents_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/contents/{+path}",
              "compare_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/compare/{base}...{head}",
              "merges_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/merges",
              "archive_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/{archive_format}{/ref}",
              "downloads_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/downloads",
              "issues_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/issues{/number}",
              "pulls_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/pulls{/number}",
              "milestones_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/milestones{/number}",
              "notifications_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/notifications{?since,all,participating}",
              "labels_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/labels{/name}",
              "releases_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/releases{/id}",
              "deployments_url":"https://api.github.com/repos/naoyukik/intellij-plugin-copy-pretty-git-log/deployments"
            },
            "url":"https://api.github.com/notifications/threads/13592645959",
            "subscription_url":"https://api.github.com/notifications/threads/13592645959/subscription"
          }
        ]
        """.trimIndent()
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString(responseJson)
    }

    private fun okhttp3.ResponseBody.toGitHubNotification(): List<GitHubNotification> {
        val responseJson = this.string()
        return Json.decodeFromString(responseJson)
    }
}
