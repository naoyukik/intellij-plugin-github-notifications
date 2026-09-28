# Specification: IntelliJ 2026.3 対応 (Issue #413)

## 概要

`untilBuild` の指定を削除し、IntelliJ IDEA 2026.3 でのプラグイン配布と動作を可能にする。

JetBrains は 2026-07-09 のアナウンスにおいて、`untilBuild` の削除を既定の推奨方針として提示している。上限値は JetBrains Marketplace の versions control で管理する運用に統一されたため、ビルドスクリプト側の指定は冗長である。

## 背景

### untilBuild に関する JetBrains の推奨

JetBrains Platform PM (yuriy.artamonov) のアナウンス (2026-07-09):

> The simplest approach — **and our recommended default** — is to drop untilBuild from your build.gradle.kts altogether. When no until-build attribute is specified, your plugin will be compatible with all future builds. You can always restrict it later through the Marketplace versions control if needed.
> — https://platform.jetbrains.com/t/2026-2-is-coming-time-to-check-your-plugin-compatibility/4618

補強材料:

- IntelliJ Platform 243 以降、プラグイン成果物に含まれる `until-build` 属性は IDE 側で無視される
- JetBrains 公式のプラグインテンプレートは 2025-05-13 に `pluginUntilBuild` を obsolete として削除済み
- 解除は `untilBuild = provider { null }` により行う。`gradle.properties` の値を空にするだけでは Plugin Verifier が `Invalid plugin descriptor` を報告する
  - https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html#intellijPlatform-pluginConfiguration-ideaVersion-untilBuild

### 2026.2 以降の Java 要件

IntelliJ Platform 2026.2 以降は Java 25 でビルドされている。

> Java version must be set depending on the target platform version.
> **2026.2+: Java 25**
> 2024.2+: Java 21
> — https://plugins.jetbrains.com/docs/intellij/api-changes-list-2026.html

現状は `kotlin { jvmToolchain(21) }` (`build.gradle.kts:22`) であり、CI も `java-version: 21` である。`platformVersion=262.6653.22` は Java 25 への切り替わり前の 2026.2 早期 EAP であるため、現状の設定でビルドが成立している。

### SDK を 263 に上げない判断

JetBrains の指針:

> Please never build with newer SDK if you target older releases. You can build with 2026.1 and your code will be 2026.2 compatible still at runtime
> — https://platform.jetbrains.com/t/2026-2-is-coming-time-to-check-your-plugin-compatibility/4618

`pluginSinceBuild=243` (2024.3、Java 21) を維持する以上、Java 25 を要求する 263 SDK へのビルドは成立しない。2026.1 SDK 相当からビルドした成果物は、実行時に 2026.3 と互換になる。

したがって本 Issue では `platformVersion` を変更しない。

### 2026.3 非互換変更の影響評価

2026.3 の公式非互換変更一覧と実コードを照合した結果、本プラグインに該当する変更は存在しない。

| 2026.3 の非互換変更 | 該当有無 | 根拠 |
|---|---|---|
| OkHttp の unbundling (`NoClassDefFoundError`) | 該当なし | HTTP クライアントを使用せず `gh` CLI を実行する方式 |
| Kotlin UI DSL 1.0 削除 (`com.intellij.ui.layout`) | 該当なし | `com.intellij.ui.dsl.builder` (DSL v2) を使用 |
| `intellij.platform.debugger` 等のモジュール分離 | 該当なし | 該当 API の使用箇所なし |
| `projectRoots.Sdk` の非継承化 | 該当なし | `Sdk` を実装していない |

## 変更内容

1. `build.gradle.kts` の `intellijPlatform.pluginConfiguration.ideaVersion.untilBuild` を `provider { null }` に変更する
2. `gradle.properties` から `pluginUntilBuild` の行を削除する
3. 生成される `plugin.xml` に `until-build` 属性が含まれないことを確認する
4. IntelliJ IDEA 2026.3 でプラグインの動作を手動検証する

## 検証項目

### 自動検証

- `./gradlew buildPlugin` が成功すること
- 生成された `plugin.xml` に `until-build` 属性が含まれないこと
- `./gradlew verifyPlugin` が成功すること

### 手動検証

- IntelliJ IDEA 2026.3 にプラグインがインストールできること
- GitHub Notifications の Tool Window が正常に表示されること
- 通知の取得 (read / unread)、フィルタ操作、設定画面が機能すること

## スコープ外

以下は本 Issue の範囲外とする。

- `platformVersion` を 263 に変更する
- `pluginSinceBuild` を 261 以降に切り上げる
- Java 25 への移行 (ビルド環境、CI、Qodana)
- JetBrains Marketplace 上の versions control 操作 (公開後の運用作業)

## 未解決事項

- Marketplace 側の versions control 設定在哪で 2026.3 への配布を許可する rean
- 2026.3 の正式リリースビルドにて再検証する必要がある
