<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

### Added

- Check for updates since the last time
  - With this update, except for the first time after startup, new notifications will appear only when there is an update in Notifications on GitHub
  - Limitations
    - Due to GitHub API specifications, you will not be notified if it has been read.
- Add unread notifications column

### Fixed

- Fixed a problem in which the list fails to be retrieved if an error is returned when retrieving detailed information.

#### System

- Add mockk

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

[Unreleased]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.5.0...HEAD
[0.5.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.3...v0.5.0
[0.4.3]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.2...v0.4.3
[0.4.2]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.4.0...v0.4.2
[0.4.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.3.0...v0.4.0
[0.3.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.2.0...v0.2.1
[0.2.0]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.0.1...v0.2.0
[0.0.1]: https://github.com/naoyukik/intellij-plugin-github-notifications/commits/v0.0.1
