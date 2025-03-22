<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog

## [Unreleased]

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

[Unreleased]: https://github.com/naoyukik/intellij-plugin-github-notifications/compare/v0.10.2...HEAD
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
