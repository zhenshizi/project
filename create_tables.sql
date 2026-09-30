-- Project Name : ねこぜーず
-- Date/Time    : 2026/09/10 9:46:36
-- Author       : H30720
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

/*
  << 注意！！ >>
  BackupToTempTable, RestoreFromTempTable疑似命令が付加されています。
  これにより、drop table, create table 後もデータが残ります。
  この機能は一時的に $$TableName のような一時テーブルを作成します。
  この機能は A5:SQL Mk-2でのみ有効であることに注意してください。
*/

-- 費目
-- * BackupToTempTable
drop table if exists "費目" cascade;

-- * RestoreFromTempTable
create table "費目" (
  himoku_id integer not null,              -- 0,1,10,11...
  category_name varchar(50) not null,      -- 給与、家賃、食費など（★修正済み）
  user_id integer not null,
  constraint "費目_PKC" primary key (himoku_id)
);

-- 日記
-- * BackupToTempTable
drop table if exists "日記" cascade;

-- * RestoreFromTempTable
create table "日記" (
  id serial not null,
  user_id integer not null,
  date date not null,
  photo bytea,
  content text,
  constraint "日記_PKC" primary key (id)
);

-- 家計簿入力
-- * BackupToTempTable
drop table if exists "家計簿入力" cascade;

-- * RestoreFromTempTable
create table "家計簿入力" (
  id serial not null,
  user_id integer not null,
  date date not null,
  memo text,
  yen numeric(10) default 0 not null,
  category_type character varying(50) not null,
  mode boolean default false not null,
  himoku_id integer,
  constraint "家計簿入力_PKC" primary key (id)
);

-- USER
-- * BackupToTempTable
drop table if exists "USER" cascade;

-- * RestoreFromTempTable
create table "USER" (
  user_id serial not null,
  user_icon bytea,
  user_name character varying(255) not null,
  mail_address character varying(256) not null,
  password character varying(255) not null,
  secret_question character varying(255) not null,
  secret_answer character varying(255) not null,
  nickname character varying(255) not null,
  create_date timestamp(6) without time zone default CURRENT_TIMESTAMP not null,
  group_id character(8),
  constraint USER_PKC primary key (user_id)
);

-- TODO
-- * BackupToTempTable
drop table if exists todo cascade;

-- * RestoreFromTempTable
create table todo (
  id serial not null,
  user_id integer not null,
  todo text,
  idea text,
  status boolean default false,
  constraint todo_PKC primary key (id)
);

-- 共有ID
-- * BackupToTempTable
drop table if exists group_id cascade;

-- * RestoreFromTempTable
create table group_id (
  group_id character(8) not null,
  created_by integer not null,
  constraint group_id_PKC primary key (group_id)
);

comment on table "費目" is '費目';
comment on column "費目".himoku_id is '費目ID:0:給与 1:収入その他 10:家賃 11:ガス代 12:電気代 13:水道代 14:通信費 15:保険料 16:教育費 17:固定その他 30:美容費 31:医療費 32:食費 33:被服 34:交際費 35:交通費 36:日用品 37:趣味 38:経費 39:変動その他 50:投資 51:貯蓄';
comment on column "費目".category_name is '費目名:給与 収入その他 家賃 ガス代 電気代 水道代 通信費 保険料 教育費 固定その他 美容費 医療費 食費 被服 交際費 交通費 日用品 趣味 経費 変動その他 投資 貯蓄';
comment on column "費目".user_id is 'ユーザID';

comment on table "日記" is '日記';
comment on column "日記".id is '日記id';
comment on column "日記".user_id is 'ユーザID';
comment on column "日記".date is '日付';
comment on column "日記".photo is '画像';
comment on column "日記".content is '日記';

comment on table "家計簿入力" is '家計簿入力';
comment on column "家計簿入力".id is '家計簿ID';
comment on column "家計簿入力".user_id is 'ユーザID';
comment on column "家計簿入力".date is '日付';
comment on column "家計簿入力".memo is 'メモ';
comment on column "家計簿入力".yen is '金額';
comment on column "家計簿入力".category_type is '支出種別:0:収入 1:固定 2:変動 3:投資・貯蓄';
comment on column "家計簿入力".mode is 'モード:0:個人 1:共有';
comment on column "家計簿入力".himoku_id is '費目ID';

comment on table "USER" is 'USER';
comment on column "USER".user_id is 'ユーザID';
comment on column "USER".user_icon is 'アイコン';
comment on column "USER".user_name is '名前';
comment on column "USER".mail_address is 'メールアドレス';
comment on column "USER".password is 'パスワード';
comment on column "USER".secret_question is '秘密の質問';
comment on column "USER".secret_answer is '秘密の質問の答え';
comment on column "USER".nickname is 'ニックネーム';
comment on column "USER".create_date is '登録日時';
comment on column "USER".group_id is '共有ID';

comment on table todo is 'TODO';
comment on column todo.id is 'TODOid';
comment on column todo.user_id is 'ユーザID';
comment on column todo.todo is '今日やること';
comment on column todo.idea is 'アイデア';
comment on column todo.status is 'TODOステータス:false:未完了 true:完了';

comment on table group_id is '共有ID';
comment on column group_id.group_id is '共有ID';
comment on column group_id.created_by is '作成した人';

insert into "費目" (himoku_id, category_name, user_id) values
(0, '給与', 1),
(1, '収入その他', 1),
(10, '家賃', 1),
(11, 'ガス代', 1),
(12, '電気代', 1),
(13, '水道代', 1),
(14, '通信費', 1),
(15, '保険料', 1),
(16, '教育費', 1),
(17, '固定その他', 1),
(30, '美容費', 1),
(31, '医療費', 1),
(32, '食費', 1),
(33, '被服', 1),
(34, '交際費', 1),
(35, '交通費', 1),
(36, '日用品', 1),
(37, '趣味', 1),
(38, '経費', 1),
(39, '変動その他', 1),
(50, '投資', 1),
(51, '貯蓄', 1);
