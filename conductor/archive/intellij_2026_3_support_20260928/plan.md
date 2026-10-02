# Plan: IntelliJ 2026.3 対応 (Issue #413)

## Phase 1: untilBuild の削除

- [x] Task: `build.gradle.kts` の `untilBuild` を `provider { null }` に変更する
    - `intellijPlatform.pluginConfiguration.ideaVersion.untilBuild` を `providers.gradleProperty("pluginUntilBuild")` から
      `provider { null }` へ変更する。
    - 解除手段は JetBrains 公式が示す `provider { null }` のみ。`orNull` は `Provider<String>` の型を保持して
      `Property<String>` に代入できずコンパイルが成立しない。
- [x] Task: `gradle.properties` から `pluginUntilBuild` を削除する
    - `pluginUntilBuild=262.*` の行を削除する。
    - 空値 (`pluginUntilBuild=`) を残さない。空値では Plugin Verifier が `Invalid plugin descriptor` を報告する。
- [x] Task: `CHANGELOG.md` の `[Unreleased]` にエントリを追加する
    - `### Removed` セクションに `pluginUntilBuild` の削除を記載する。
    - `### Changed` セクションに `Support for IntelliJ 2026.3` を記載する。
- [ ] Task: Conductor - User Manual Verification 'Phase 1: untilBuild の削除' (Protocol in workflow.md)

## Phase 2: ビルド環境の修復

`main` ブランチの時点で `buildPlugin` が失敗していた。untilBuild の変更を成立させるため、依存解決の障害を解消する。

- [x] Task: `platformVersion` を実在するリリースへ更新する
    - `262.6653.22` は JetBrains のリポジトリから消滅しているため
      `Could not find any version that matches ... java-compiler-ant-tasks` で失敗する。
    - `261.27258.48` (2026.1 系最終 stable、Java 21) へ変更する。
    - 2026.2 以降の SDK は Java 25 を要求し `sinceBuild=243` と両立しないため採用しない。
- [x] Task: IntelliJ Platform Gradle Plugin を 2.19.0 へ更新する
    - 2.16.0 は 261 以降の IntelliJ Platform 依存を解決できず `No IntelliJ Platform dependency found` で失敗する。
    - `gradle/libs.versions.toml` の `intelliJPlatform` を `2.16.0` から `2.19.0` へ変更する。
- [x] Task: `pluginVerification.ides` を 2.19.0 の API へ書き換える
    - 2.19.0 で `ProductReleasesValueSource` が削除されたため既存実装がコンパイル不能になる。
    - 旧実装は全リリースから先頭と末尾を 1 つずつ選んでいた。`recommended()` は `since-build` から最新までを全て対象とするため、
      `untilBuild` 削除後は対象が 7 リリースに広がる。
    - 最古を `create(..., "2024.3")`、最新を `latest { types = ... }` として意図を維持する。
- [ ] Task: Conductor - User Manual Verification 'Phase 2: ビルド環境の修復' (Protocol in workflow.md)

## Phase 3: ビルドと自動検証

- [x] Task: `buildPlugin` の実行と descriptor の確認
    - `./gradlew buildPlugin` を実行し、ビルドが成功することを確認する。
    - `build/tmp/patchPluginXml/plugin.xml` を確認し、`<idea-version since-build="243" />` となり `until-build`
      属性が含まれないことを検証する。
- [x] Task: `check` の実行
    - `./gradlew check` を実行し、テストとカバレッジ検証が成功することを確認する。
- [ ] Task: `verifyPlugin` の実行
    - `./gradlew verifyPlugin` を実行し、Plugin Verifier が成功することを確認する。
    - 検証対象の IDE が 2024.3 と 2026.3 相当 (263) の 2 件であることを確認する。
- [ ] Task: Conductor - User Manual Verification 'Phase 3: ビルドと自動検証' (Protocol in workflow.md)

## Phase 4: IntelliJ IDEA 2026.3 での手動検証

- [ ] Task: 2026.3 でのプラグイン動作確認
    - IntelliJ IDEA 2026.3 EAP (263.5701.42 以降) をインストールする。
    - ビルドしたプラグインをZIPから直接インストールし、インストールが成功することを確認する。
    - GitHub Notifications の Tool Window が正常に表示されることを確認する。
    - 通知の取得 (read / unread 切替)、リポジトリ / ラベルのフィルタ操作、設定画面の調整が機能することを確認する。
    - IDE ログに本プラグインに起因する例外がないことを確認する。
- [ ] Task: 後続 Issue の起票
    - Marketplace 上の versions control 設定 (2026.3 の配布許可) を行う Follow-up Issue を作成する。
    - 2026.3 正式版リリース時の再検証を Follow-up Issue として記録する。
- [ ] Task: Conductor - User Manual Verification 'Phase 4: IntelliJ IDEA 2026.3 での手動検証' (Protocol in workflow.md)
