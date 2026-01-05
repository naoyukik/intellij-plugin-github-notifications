# Specification: Fix Deprecated API Usage (Issue #387)

## 概要

`GitHubNotificationsToolWindowFactory` クラスにおいて、非推奨（Deprecated）となっている `ToolWindowFactory.isApplicable`
メソッドが使用されている。これを推奨される API (`isApplicableAsync` または `plugin.xml` での条件指定)
に置き換え、警告を解消するとともに将来的な互換性を確保する。

## 現状の問題

- クラス: `com.github.naoyukik.intellijplugingithubnotifications.userInterface.GitHubNotificationsToolWindowFactory`
- 問題: `isApplicable(Project)` メソッドをオーバーライドして使用している。
- 影響: IntelliJ Platform の将来のバージョンで削除される可能性があるため、互換性の問題が発生する。

## 変更内容

1. `GitHubNotificationsToolWindowFactory` クラスの `isApplicable` メソッドを削除、または `isApplicableAsync` へ移行する。
2. 必要であれば `plugin.xml` の定義を確認し、ToolWindow の表示条件が正しく機能することを確認する。

## 技術的詳細

- `ToolWindowFactory.isApplicable` は同期的に実行されるため、UIスレッドをブロックする可能性がある。
- 推奨される `isApplicableAsync` は `suspend` 関数であり、非同期に判定を行うことができる。

## 検証項目

- IntelliJ IDEA を起動し、GitHub Notifications ツールウィンドウが正常に表示されること。
- 非推奨 API の使用に関する警告がビルドログまたは IDE 上で消えていること。
