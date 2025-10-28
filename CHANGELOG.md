<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

## [0.14.7] - 2025-10-26

### Fixed

- Handle unknown notification subject types gracefully to prevent crashes from new GitHub API values.
- Fix a bug that caused a crash when the notification subject URL is null.

## [0.14.6] - 2025-10-05

### Changed

- Supported versions expanded from 2024.3

## [0.14.5] - 2025-10-02

### Changed

- Support for IntelliJ 2025.3

## [0.14.3] - 2025-05-20

### Changed

- Support for IntelliJ 2025.2

## [0.14.2] - 2025-04-20

### Fixed

- Fix a bug where Instances are shared between projects

## [0.14.1] - 2025-04-15

### Fixed

- Reviewers filter does not show contents of requested_teams

## [0.14.0] - 2025-04-14

### Added

- Add reviewers of Teams selectable
- Support multiple reviewers in notification filtering
    - Reviewer filters are comma-separated and can be filtered by multiple reviewers.

## [0.13.0] - 2025-04-06

### Added

- Set the refresh button to always acquire

### Fixed

- Prevent crashing on invalid table cell clicks

## [0.12.0] - 2025-03-30

### Added

- Support multiple labels in notification filtering
  - Label filters are comma-separated and can be filtered by multiple labels.
- Introduce Virtual Threads to get notification details

### Fixed

- replace fixedRateTimer with coroutine for auto-refresh

## [0.11.0] - 2025-03-23

### Added

- Add dynamic label and reviewer filtering in notification panel
- Add a badge to the tool window icon to indicate updates

## [0.10.2] - 2025-03-17

### Added

- Add Read filter

### Fixed

- Set a minimum value for column height

## [0.10.0] - 2025-03-16

### Added

- Reset the latest fetch time on settings apply
- Implement label column
- Implement label and unread filter

### Fixed

- Optimize type filter

## [0.9.0] - 2025-03-12

### Added

- Implement Reviewers filter

## [0.8.0] - 2025-03-10

### Added

- Implement Type filter

## [0.7.0] - 2025-02-21

### Added

- Setting a limit on the number of results

### Fix

- Remade the settings screen

## [0.6.0] - 2025-02-16

### Added

- Check for updates since the last time
  - With this update, except for the first time after startup, new notifications will appear only when there is an update in Notifications on GitHub
  - Limitations
    - Due to GitHub API specifications, you will not be notified if it has been read.
- Add support for including read notifications in fetch
  - Add unread notifications column

### Fixed

- Fixed a problem in which the list fails to be retrieved if an error is returned when retrieving detailed information.

## [0.5.0] - 2025-01-30

### Added

- Get Notification status for Pull Request and Issue from Details
- Added display of reviewer names for pull requests

## [0.4.3] - 2025-01-18

### Breaking Changes

- Change the location of the project settings
  - If you were using up to 0.4.2, your settings will be initialized🙇

## [0.4.2] - 2025-01-16

### Changed

- Support for IntelliJ 2025

### Fixed

- Refactoring fetchNotifications By Repository In Api Client Workflow

## [0.4.0] - 2025-01-13

### Added

- Support for Release type links

## [0.3.0] - 2025-01-10

### Added

- Display emoji according to type
- Add support for configuring a custom GitHub CLI path

## [0.2.1] - 2025-01-05

### Fixed

- Fix double refresh on startup

## [0.2.0] - 2025-01-04

### Added

- Automatically fetching data
- Support for notification balloons
- Repository filtering

### Fixed

- Fix nullable htmlUrl handling in DTO and related logic

## [0.0.1] - 2024-12-29

Initial release of the GitHub Notifications IntelliJ Plugin.

Done is better than perfect.

[Unreleased]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.7...HEAD
[0.14.7]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.6...v0.14.7
[0.14.6]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.5...v0.14.6
[0.14.5]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.3...v0.14.5
[0.14.3]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.2...v0.14.3
[0.14.2]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.1...v0.14.2
[0.14.1]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.14.0...v0.14.1
[0.14.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.13.0...v0.14.0
[0.13.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.12.0...v0.13.0
[0.12.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.11.0...v0.12.0
[0.11.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.10.2...v0.11.0
[0.10.2]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.10.0...v0.10.2
[0.10.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.9.0...v0.10.0
[0.9.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.8.0...v0.9.0
[0.8.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.7.0...v0.8.0
[0.7.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.6.0...v0.7.0
[0.6.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.5.0...v0.6.0
[0.5.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.3...v0.5.0
[0.4.3]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.2...v0.4.3
[0.4.2]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.0...v0.4.2
[0.4.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.0.1...v0.2.0
[0.0.1]: https://github.com/naoyukik/intellij-plugin-github-notifications/commits/v0.0.1
