# Product Guidelines

## User Interface (UI) & User Experience (UX)

- **Native Look & Feel**: The plugin must strictly adhere to
  the [IntelliJ Platform UI Guidelines](https://plugins.jetbrains.com/docs/intellij/user-interface-guidelines.html). It
  should feel like a built-in part of the IDE, not a foreign web page.
- **Theme Support**: The UI must render correctly in both **Light** and **Darcula** (Dark) themes. Icons should have
  variants for both themes.
- **Tool Window**: The primary interface is a Tool Window. It should be collapsible and not block the editor view.
- **Feedback**: Provide immediate visual feedback for actions (e.g., loading spinners during network requests, toast
  notifications for errors).

## coding Standards

- **Language**: Kotlin is the primary language.
- **Style**: Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Asynchronous Operations**: All network I/O (GitHub API calls) must be performed on background threads (using
  Coroutines or `Task.Backgroundable`) to strictly avoid freezing the UI thread (EDT).

## Reliability & Performance

- **Rate Limiting**: The plugin must handle GitHub API rate limits gracefully, informing the user if limits are reached.
- **Offline Handling**: The plugin should handle network connectivity issues without crashing, displaying a helpful "
  Offline" state or caching previous results if possible.
- **Token Security**: GitHub Personal Access Tokens (PAT) must be stored securely using the IntelliJ Credential Store
  API.

## Accessibility

- **Keyboard Navigation**: All interactive elements in the tool window should be accessible via keyboard (Tab
  navigation, Enter to activate).
- **Contrast**: Ensure sufficient contrast for text and icons in all themes.

## Documentation

- **Language**: All Conductor tracks, plans, and specifications should be written in **Japanese**.

