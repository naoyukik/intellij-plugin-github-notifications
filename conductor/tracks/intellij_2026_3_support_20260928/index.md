# Track: IntelliJ 2026.3 対応 (Issue #413)

## 概要

`untilBuild` の指定を削除し、IntelliJ IDEA 2026.3 でのプラグイン配布と動作を可能にする。

JetBrains の推奨方針に従い、`platformVersion` は変更せず 2026.1 SDK 相当からビルドした成果物による実行時互換性で 2026.3 に対応する。

## 参照

- [仕様書 (spec.md)](./spec.md)
- [実装計画 (plan.md)](./plan.md)
- [メタデータ (metadata.json)](./metadata.json)
- [GitHub Issue #413](https://github.com/naoyukik/intellij-plugin-github-notifications/issues/413)

## 状態

- タイプ: chore
- ステータス: new
- ブランチ: `413-support-for-intellij-20263`
