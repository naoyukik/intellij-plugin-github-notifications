# GitHub Notifications IntelliJ Plugin

![Build](https://github.com/naoyukik/intellij-plugin-github-notifications/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/26214-github-notifications.svg)](https://plugins.jetbrains.com/plugin/26214-github-notifications)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/26214-github-notifications.svg)](https://plugins.jetbrains.com/plugin/26214-github-notifications)

<!-- Plugin description -->
The **GitHub Notifications IntelliJ Plugin** allows you to effortlessly view your GitHub notifications directly within the IntelliJ IDEA development environment. With this plugin, there's no need to frequently switch back and forth between your browser and IDE, improving your productivity during development.

### Key Features
- **Real-time Notification Tracking**: Displays a list of unread GitHub notifications.
- **Intuitive Link Navigation**: Quickly open relevant GitHub resources (pull requests, issues, etc.) directly from the notification list.
- **Simple Usability**: Refresh your notifications with just one click.
- **Organized Notification View**: Presents notifications in a readable table format, including details like the link, reason, and timestamp.
- **Repository Filtering**: Quickly filter notifications by specific GitHub repositories for a more focused view.
- **Scheduled Updates**: Automatically refresh notifications at user-defined intervals to stay up-to-date without manual intervention.

### How to Use

#### Reviewer Filters

- **Basic usage**: Choose a reviewer name to filter notifications with that specific reviewer
- **Multiple reviewers**: Separate with commas to filter by ANY of the specified reviewers

  ```
  Format: reviewer1,reviewer2,reviewer3
  ```

#### Label Filters
- **Basic usage**: Choose a label name to filter notifications with that specific label
- **Multiple labels**: Separate with commas to filter by ANY of the specified labels

  ```
  Format: label1,label2,label3
  ```

### Limitations

- **Limit on the number of notifications**: Only the latest 50 notifications can be retrieved.
- **No tracking of updates**: Displaying updates based on differences from the current state is not supported.

These limitations are planned to be addressed in **future updates**.

**Important:**
This plugin requires [GitHub CLI](https://cli.github.com/) to be installed and properly authenticated on your system.
Please ensure it is set up before using this plugin.

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
