# Plan: Fix Deprecated API Usage (Issue #387)

## Phase 1: 現状確認と調査 [checkpoint: ee8b8b1]
- [x] Task: プロジェクトのビルドと警告の確認
    - `./gradlew buildPlugin` を実行し、非推奨 API の警告が出ている箇所を特定する。
    - `GitHubNotificationsToolWindowFactory.kt` のコードを確認し、現在の `isApplicable` のロジックを把握する。
- [x] Task: Conductor - User Manual Verification 'Phase 1: 現状確認と調査' (Protocol in workflow.md)

## Phase 2: 実装 (API修正)
- [x] Task: `isApplicableAsync` への移行 [680954c]
    - `GitHubNotificationsToolWindowFactory` の `isApplicable` を `isApplicableAsync` に変更する。
    - Kotlin Coroutines を使用して非同期処理として実装する（必要な場合）。
- [x] Task: ビルドと静的解析の実行 [6ea9f07]
    - `./gradlew buildPlugin` を実行し、ビルドが成功することを確認する。
    - `./gradlew detekt` を実行し、リントエラーがないことを確認する。
- [~] Task: Conductor - User Manual Verification 'Phase 2: 実装 (API修正)' (Protocol in workflow.md)

## Phase 3: 動作検証
- [ ] Task: ツールウィンドウの表示確認
    - `./gradlew runIde` で IDE を起動する。
    - プラグインが読み込まれ、ツールウィンドウが表示されることを確認する。
    - ツールウィンドウの表示/非表示の条件（もしあれば）が正しく機能しているか確認する。
- [ ] Task: Conductor - User Manual Verification 'Phase 3: 動作検証' (Protocol in workflow.md)
