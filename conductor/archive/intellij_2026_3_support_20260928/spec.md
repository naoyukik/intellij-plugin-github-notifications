# Specification: IntelliJ 2026.3 対応 (Issue #413)

## 概要

`untilBuild` の指定を削除し、IntelliJ IDEA 2026.3 でのプラグイン配布と動作を可能にする。

JetBrains は 2026-07-09 のアナウンスにおいて、`untilBuild` の削除を既定の推奨方針として提示している。上限値は JetBrains
Marketplace の versions control で管理する運用に統一されたため、ビルドスクリプト側の指定は冗長である。

ただし本 Issue の着手時点で、`main` ブランチは `buildPlugin` が失敗する状態にあった。これは本 Issue
と無関係に発生していた障害である。untilBuild の変更を達成するため、ビルドを成立させる修正を本 Issue に含める。

## 背景

### untilBuild に関する JetBrains の推奨

JetBrains Platform PM (yuriy.artamonov) のアナウンス (2026-07-09):

> The simplest approach — **and our recommended default** — is to drop untilBuild from your build.gradle.kts altogether.
> When no until-build attribute is specified, your plugin will be compatible with all future builds. You can always
> restrict it later through the Marketplace versions control if needed.
> — https://platform.jetbrains.com/t/2026-2-is-coming-time-to-check-your-plugin-compatibility/4618

補強材料:

- IntelliJ Platform 243 以降、プラグイン成果物に含まれる `until-build` 属性は IDE 側で無視される
- JetBrains 公式のプラグインテンプレートは 2025-05-13 に `pluginUntilBuild` を obsolete として削除済み
- 解除は `untilBuild = provider { null }` により行う。`gradle.properties` の値を空にするだけでは Plugin Verifier が
  `Invalid plugin descriptor` を報告する
    - https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html#intellijPlatform-pluginConfiguration-ideaVersion-untilBuild

### 着手時点で main ブランチのビルドが成立していないこと

`25f9c69` (origin/main) を基準に `untilBuild` の変更前後いずれでも `./gradlew buildPlugin` は失敗する。変更前から存在していた障害であり、本
Issue の変更が原因ではない。

| 条件                                 | 失敗箇所 | 内容                                                                                   |
|--------------------------------------|----------|----------------------------------------------------------------------------------------|
| 変更前 (`262.6653.22`, IPGP 2.16.0)  | 実行時   | `java-compiler-ant-tasks:{strictly [262, 262.6653.22]; prefer 262.6653.22}` が解決不能 |
| 変更前 (`262.6653.22`, IPGP 2.16.0)  | 設定時   | `ProductReleasesValueSource` の `types` 解決失敗 (`build.gradle.kts:120`)              |
| 変更後 (`261.27258.48`, IPGP 2.16.0) | 設定時   | 同上。IntelliJ Platform 依存が解決できていない                                         |
| 変更後 (`261.27258.48`, IPGP 2.19.0) | なし     | 成功                                                                                   |

第 1 の直接原因は `262.6653.22` が JetBrains のリポジトリから消滅したことです。`intellij-repository/releases` の `ideaIU`
における 262 系の最新は `262.10968.63`、`java-compiler-ant-tasks` における 262 系の最新は `262.10968.117` であり、
`262.6653.22` はいずれも存在しない。CI が 2026-06-06 に成功していたことと矛盾する。

第 2、3 の直接原因は IntelliJ Platform Gradle Plugin 2.16.0 が 261 以降のプラットフォーム依存を解決できないことです。
`types` の解決はプロジェクトの IntelliJ Platform 依存を前提とするため、依存が解決できない場合にこのエラーになる。

### platformVersion の選定

`pluginSinceBuild=243` (2024.3、Java 21) を維持する前提で、実在するリリースのうち Java 21 でビルドできるものを選ぶ。

| 候補                                  | 判定                                          |
|---------------------------------------|-----------------------------------------------|
| `262.6653.22`                         | 破棄済み。採用不可                            |
| `262.10968.117` (2026.2 最終 stable)  | Java 25 が必要。`sinceBuild=243` と両立しない |
| `263.5701.42` (2026.3 EAP)            | Java 25 が必要。`sinceBuild=243` と両立しない |
| `261.27258.48` (2026.1 系最終 stable) | 実在し Java 21。**採用する**                  |

Java 25 を要求する 2026.2 以降の SDK でビルドすることは、`pluginSinceBuild=243` の利用者を事実上失う。JetBrains
も「古いリリースを対象に新しい SDK でビルドするな」と明記している。

> Please never build with newer SDK if you target older releases. You can build with 2026.1 and your code will be 2026.2
> compatible still at runtime
> — https://platform.jetbrains.com/t/2026-2-is-coming-time-to-check-your-plugin-compatibility/4618

したがって 2026.1 系 (261) の SDK でビルドし、実行時に 2026.3 と互換になることで 2026.3 対応を達成する。

### IntelliJ Platform Gradle Plugin の更新

IPGP 2.16.0 は 261 以降のプラットフォーム依存を解決できないため 2.19.0 へ更新する。

2.19.0 では `ProductReleasesValueSource` が削除され、`ProductReleasesFilterParameters` と内部の
`ProductReleasesListingValueSource` に分割された。このため `pluginVerification.ides` の既存実装はコンパイル不能になる。

### pluginVerification.ides の変更方針

変更前の実装は `ProductReleasesValueSource().get()` を設定時に呼び出し、全 IntelliJ IDEA Ultimate リリースから最古と最新を
1 つずつ抜き出していた。意図は「サポート範囲の最古と最新で検証する」ことである。

2.19.0 の `Ides` 拡張は `recommended()` を既定とするが、これは `since-build` から最新までの全リリースを対象とする。
`untilBuild` を削除した本変更の後は対象が 7 リリースに広がり、CI での IDE ダウンロード量が大幅に増える。

そこで意図を維持するため、最古は `pluginSinceBuild=243` に対応する 2026.1 以前の版である 2024.3 を明示し、最新は
`latest {}` で動的に解決する。この組み合わせは変更前の「先頭と末尾」の選択と一致する。

`printProductsReleases` の出力 (RELEASE / EAP / RC) は次の 7 件であり、`latest {}` は先頭の `IU-263.5701.42` (2026.3 EAP)
に解決される。

```
IU-263.5701.42
IU-2026.2.3
IU-2026.1.5
IU-2025.3.6.1
IU-2025.2.6.3
IU-2025.1.7.2
IU-2024.3.7.1
```

### 2026.3 非互換変更の影響評価

2026.3 の公式非互換変更一覧と実コードを照合した結果、本プラグインに該当する変更は存在しない。

| 2026.3 の非互換変更                               | 該当有無 | 根拠                                                |
|---------------------------------------------------|----------|-----------------------------------------------------|
| OkHttp の unbundling (`NoClassDefFoundError`)     | 該当なし | HTTP クライアントを使用せず `gh` CLI を実行する方式 |
| Kotlin UI DSL 1.0 削除 (`com.intellij.ui.layout`) | 該当なし | `com.intellij.ui.dsl.builder` (DSL v2) を使用       |
| `intellij.platform.debugger` 等のモジュール分離   | 該当なし | 該当 API の使用箇所なし                             |
| `projectRoots.Sdk` の非継承化                     | 該当なし | `Sdk` を実装していない                              |

## 変更内容

1. `build.gradle.kts` の `intellijPlatform.pluginConfiguration.ideaVersion.untilBuild` を `provider { null }` に変更する
2. `gradle.properties` から `pluginUntilBuild` の行を削除する
3. `gradle.properties` の `platformVersion` を `262.6653.22` から `261.27258.48` に変更する
4. `gradle/libs.versions.toml` の IntelliJ Platform Gradle Plugin を `2.16.0` から `2.19.0` に更新する
5. `build.gradle.kts` の `pluginVerification.ides` を 2.19.0 の API に合わせて書き換える
6. `CHANGELOG.md` の `[Unreleased]` にエントリを追加する
7. 生成される `plugin.xml` に `until-build` 属性が含まれないことを確認する
8. IntelliJ IDEA 2026.3 でプラグインの動作を手動検証する

## 検証項目

### 自動検証

- `./gradlew buildPlugin` が成功すること
- 生成された `plugin.xml` に `until-build` 属性が含まれず、`since-build="243"` が維持されていること
- `./gradlew check` が成功すること
- `./gradlew verifyPlugin` が成功し、検証対象 IDE に 2026.3 相当 (263) が含まれること

### 手動検証

- IntelliJ IDEA 2026.3 にプラグインがインストールできること
- GitHub Notifications の Tool Window が正常に表示されること
- 通知の取得 (read / unread)、フィルタ操作、設定画面が機能すること

## スコープ外

以下は本 Issue の範囲外とする。

- `platformVersion` を 262 以降 (Java 25) に変更すること
- `pluginSinceBuild` を 261 以降に切り上げること
- Java 25 への移行 (ビルド環境、CI、Qodana)
- JetBrains Marketplace 上の versions control 操作 (公開後の運用作業)
- `main` ブランチのビルド障害を遡って修復すること。本 Issue のブランチでのみ修正する

## 未解決事項

- Marketplace 側の versions control 設定をどこで 2026.3 への配布を許可するか (公開後の運用作業)
- 2026.3 の正式リリースビルドにて再検証する必要がある
- `main` ブランチに同じ修正を適用する必要がある (本 Issue の PR で包含される)
