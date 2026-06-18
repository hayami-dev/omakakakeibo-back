<div style="text-align:center; margin-left:-3rem">
<img src="src/main/resources/logo.svg"/>
</div>

# おおまか家計簿アプリケーション

直感的でストレスのない操作性と、堅牢なバリデーションを両立した家計簿（支出入力）アプリケーションです。
ゆるっとしたUIで、従来の家計簿アプリと異なった「しっかりしなくていい」「だから継続しやすい」をコンセプトにしています。

## 🛠️ 技術スタック

<h4>フロントエンド</h4>
<p>
  <img src="https://img.shields.io/badge/-JavaScript-F7DF1E.svg?logo=javascript&style=for-the-badge&logoColor=black">
  <img src="https://img.shields.io/badge/-React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB">
  <img src="https://img.shields.io/badge/-Vite-646CFF.svg?logo=vite&style=for-the-badge&logoColor=white">
  <img src="https://img.shields.io/badge/-Jotai-555555.svg?style=for-the-badge&logo=snapchat&logoColor=white">
  <img src="https://img.shields.io/badge/-TailwindCSS-06B6D4.svg?logo=tailwindcss&style=for-the-badge&logoColor=white">
</p>

<h4>バックエンド</h4>
<p>
  <img src="https://img.shields.io/badge/-Java-ED8B00.svg?logo=openjdk&style=for-the-badge&logoColor=white">
  <img src="https://img.shields.io/badge/-Spring%20Boot-6DB33F.svg?logo=spring-boot&style=for-the-badge&logoColor=white">
  <img src="https://img.shields.io/badge/-MyBatis-2B2B2B.svg?style=for-the-badge&logo=databricks&logoColor=red">
  <img src="https://img.shields.io/badge/-Apache%20Maven-C71A36.svg?logo=apache-maven&style=for-the-badge&logoColor=white">
</p>

<h4>データベース</h4>
<p>
  <img src="https://img.shields.io/badge/-MySQL-4479A1.svg?logo=mysql&style=for-the-badge&logoColor=white">
</p>

---

## 🔗 関連ドキュメント・参考リンク

プロジェクトに関する詳細な仕様や設計、各種ドキュメントへのリンクです。
※ Googleアカウント、一部Figmaアカウントが必要です。

- 📝 [画面仕様書](https://docs.google.com/spreadsheets/d/1nrlp1_iS8Rk5Fv9bXnckUMsHUOovt6qYE0Fqf0dzjOE/edit?usp=drive_link)
- 📝 [DB設計書](https://docs.google.com/spreadsheets/d/1rxP_q-hG97UIizI5bg6uvUVi94M8-oe4bprtLHYFhcU/edit?usp=drive_link)
- 📝 [プレゼン資料(FigmaSlide)](https://www.figma.com/deck/kCdFowOIBJzjYd6xhxYNak)
- 🎨 [デザインモック(Figma)](https://www.figma.com/design/IUr5Z9JQFcxIYsQYOpKYTr/%E3%81%8A%E3%81%8A%E3%81%BE%E3%81%8B%E5%AE%B6%E8%A8%88%E7%B0%BF?m=auto&t=dZAxhEOcx5S7FLyY-6)
- 🔌 [画面フロー資料(FigJam)](https://www.figma.com/board/AjoqgA5DE7BeC1wHt8nV5r/%E3%81%8A%E3%81%8A%E3%81%BE%E3%81%8B%E5%AE%B6%E8%A8%88%E7%B0%BF-flow?t=dZAxhEOcx5S7FLyY-1)

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## 目次

1. [プロジェクトについて](#プロジェクトについて)
2. [環境](#環境)
3. [ディレクトリ構成](#ディレクトリ構成)
4. [開発環境の動かし方](#開発環境の動かし方)

---

## プロジェクトについて

「おおまか家計簿アプリケーション」は、フロントエンドを **React (Vite)**、バックエンドを **Java (Spring Boot + MyBatis)** で構築した、きっちりしすぎない気軽な記録をサポートする家計簿アプリです。

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## 環境

| レイヤー      | 技術・ツール                 | 役割                                 |
| :------------ | :--------------------------- | :----------------------------------- |
| **Front-end** | JavaScript / React 18 / Vite | UI構築・ビルド                       |
|               | Jotai                        | 状態管理（アトミック）               |
|               | Tailwind CSS                 | スタイリング                         |
| **Back-end**  | Java / Spring Boot           | APIサーバー                          |
|               | MyBatis                      | O/Rマッパー・SQL管理                 |
|               | Apache Maven                 | 依存関係管理・ビルドツール           |
| **Database**  | MySQL                        | データの永続化（家計簿データの保存） |

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## ディレクトリ構成

### フロントエンド

```text
omakakakeibo/ (プロジェクトルート)
├── dist/                         # 本番公開用のビルド成果物
│   ├── assets/
│   │   ├── index-CufLO1l-.js
│   │   └── index-qpnLmUlQ.css
│   ├── favicon.svg
│   └── index.html
├── public/
│   ├── favicon.svg
│   └── icons.svg
├── src/                          # ソースコード
│   ├── main.jsx
│   ├── App.jsx
│   ├── components/               # 汎用・共通部品
│   │   ├── input/
│   │   └── ui/
│   ├── pages/
│   ├── service/
│   └── styles/                   # TailwindやCSSの設定
├── node_modules/
├── package.json
├── vite.config.js
└── tailwind.config.ts
```

### バックエンド

```text
omakakakeibo-back/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/           # Javaソースコード
    │   └── resources/      # 設定ファイル・SQL
    │       ├── application.properties
    │       └── sql/         # データベース関連のSQLファイル群
    │           ├── 1_db_create.sql
    │           ├── 2_db_user.sql
    │           ├── 3_db_category_master.sql
    │           ├── 4_db_active_category.sql
    │           ├── 5_db_budget.sql
    │           └── 6_db_history.sql
    └── test/
        └── java/
```

<p align="right">(<a href="#top">トップへ</a>)</p>

---

## 開発環境の動かし方

### フロントエンド（omakakakeibo-front）の起動

1. omakakakeibo-front ディレクトリに移動します。
2. 必要なパッケージをインストールします。

```
npm install
```

3. 開発サーバーを起動します。

```
npm run dev
```

### バックエンド（omakakakeibo-back）の起動

1. **データベース（MySQL）の準備**
   お使いのMySQL環境にアプリケーション用のデータベースを作成します。
   その後、各sqlファイルを実行して、テーブルの作成と初期データの投入を行ってください。
   詳細は[データベース（MySQL）のセットアップ手順](#データベースmysqlのセットアップ手順) を参照してください。

2. Eclipse (Pleiades All in One) 等の環境で omakakakeibo-back を読込、またはMavenコマンドを使用してSpring Bootアプリケーションを実行します。

3. 設定などは src/main/resources/application.properties を参照してください。

### データベース（MySQL）のセットアップ手順

本プロジェクトには、検証・テスト用のSQLファイルが多数用意されています。
テーブル間の依存関係（外部キー制約）があるため、必ず**以下の順番（1 〜 6）で実行**してください。

| 実行順 | ファイル名                 | 処理内容・実行の目的                                                                                  |
| :----: | :------------------------- | :---------------------------------------------------------------------------------------------------- |
| **1**  | `1_db_create.sql`          | **データベースと土台の作成**<br>全ての中心となる `users` テーブル等を生成します。                     |
| **2**  | `2_db_user.sql`            | **テストユーザーの作成**<br>これ以降のデータを紐付けるためのベースとなるユーザーを登録します。        |
| **3**  | `3_db_category_master.sql` | **カテゴリマスタの登録**<br>家計簿の項目（食費・日用品など）のマスターデータを登録します。            |
| **4**  | `4_db_active_category.sql` | **アクティブカテゴリの登録**<br>ユーザーが現在選択している「6つの設定枠（スロット）」を確定させます。 |
| **5**  | `5_db_budget.sql`          | **月別予算データの登録**<br>月ごとの目標金額（例: 2026年3月〜5月分）を登録します。                    |
| **6**  | `6_db_history.sql`         | **支出明細（履歴）の登録**<br>家計簿のメインデータである、日々の支出履歴データを流し込みます。        |

⚠️ **注意点**

- 各ファイルには「【異常系】エラーテスト用」の、**わざとエラーを起こすSQL文**も含まれています。
- 異常系のエリアを実行した際、MySQL Workbenchなどで赤バツ（❌）やエラーコードが出れば、データベースの制約が正常に機能している証拠（テスト成功）です。

<p align="right">(<a href="#top">トップへ</a>)</p>

---

<p align="right">以上</p>
