# GitHub Notifications IntelliJ Plugin

![Build](https://github.com/naoyukik/intellij-plugin-github-notifications/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/26214-github-notifications.svg)](https://plugins.jetbrains.com/plugin/26214-github-notifications)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/26214-github-notifications.svg)](https://plugins.jetbrains.com/plugin/26214-github-notifications)

<!-- Plugin description -->
The **GitHub Notifications IntelliJ Plugin** allows you to effortlessly view your GitHub notifications directly within the IntelliJ IDEA development environment. With this plugin, there's no need to frequently switch back and forth between your browser and IDE, improving your productivity during development.

Key Features:
- **Real-time Notification Tracking**: Displays a list of unread GitHub notifications.
- **Intuitive Link Navigation**: Quickly open relevant GitHub resources (pull requests, issues, etc.) directly from the notification list.
- **Simple Usability**: Refresh your notifications with just one click.
- **Organized Notification View**: Presents notifications in a readable table format, including details like the link, reason, and timestamp.

**Important:**
This plugin requires the **`gh CLI`** (GitHub CLI) to be installed and properly authenticated on your system. Please ensure it is set up before using this plugin.

This plugin is designed to empower developers to remain focused on their work environment while staying up-to-date with important updates on GitHub.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij-plugin-github-notifications"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/naoyukik/intellij-plugin-github-notifications/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
