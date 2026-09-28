# Plan: IntelliJ 2026.3 対応 (Issue #413)

## Phase 1: untilBuild の削除

- [ ] Task: `build.gradle.kts` の `untilBuild` を `provider { null }` に変更する
    - `intellijPlatform.pluginConfiguration.ideaVersion.untilBuild` を `providers.gradleProperty("pluginUntilBuild")` から `provider { null }` へ変更する。
    - `provider { null }` もしくは `providers.gradleProperty("pluginUntilBuild").orNull` ではなく、JetBrains 公式が示す `provider { null }` を使用する理由を記録する。
- [ ] Task: `gradle.properties` から `pluginUntilBuild` を削除する
    - `pluginUntilBuild=262.*` の行を削除する。
    - 空値 (`pluginUntilBuild=`) を残さない。空値では Plugin Verifier が `Invalid plugin descriptor` を報告する。
- [ ] Task: `CHANGELOG.md` の `[Unreleased]` にエントリを追加する
    - `### Removed` セクションに `Remove the obsolete pluginUntilBuild property` を記載する。
    - `### Changed` セクションに `Support for IntelliJ 2026.3` を記載する。
- [ ] Task: Conductor - User Manual Verification 'Phase 1: untilBuild の削除' (Protocol in workflow.md)

## Phase 2: ビルドと自動検証

- [ ] Task: `buildPlugin` の実行と descriptor の確認
    - `./gradlew buildPlugin` を実行し、ビルドが成功することを確認する。
    - `build/patchPluginXml/plugin.xml` を確認し、`<idea-version>` に `until-build` 属性が含まれないことを検証する。
    - `since-build="243"` が維持されていることを確認する。
- [ ] Task: `verifyPlugin` の実行
    - `./gradlew verifyPlugin` を実行し、Plugin Verifier が成功することを確認する。
    - 検証対象の IDE リストに 2026.3 相当が含まれるかを確認し、含まれない場合はその理由を記録する。
- [ ] Task: 静的解析とテストの実行
    - `./gradlew check` を実行し、テストと静的解析がすべて成功することを確認する。
- [ ] Task: Conductor - User Manual Verification 'Phase 2: ビルドと自動検証' (Protocol in workflow.md)

## Phase 3: IntelliJ IDEA 2026.3 での手動検証

- [ ] Task: 2026.3 でのプラグイン動作確認
    - IntelliJ IDEA 2026.3 EAP (263.5701.42 以降) をインストールする。
    - ビルドしたプラグインをZIPから直接インストールし、インストールが成功することを確認する。
    - GitHub Notifications の Tool Window が正常に表示されることを確認する。
    - 通知の取得 (read / unread 切替)、リポジトリ / ラベルのフィルタ操作、設定画面の調整が機能することを確認する。
    - IDE ログに本プラグインに起因する例外がないことを確認する。
- [ ] Task: 後続 Issue の起票
    - 手動検証の結果に応じて、Marketplace 上の versions control 設定 (2026.3 の配布許可) を行う Follow-up Issue を作成する。
    - 2026.3 正式版リリース時の再検証を.follow-up Issue として記録する。
- [ ] Task: Conductor - User Manual Verification 'Phase 3: IntelliJ IDEA 2026.3 での手動検証' (Protocol in workflow.md)
