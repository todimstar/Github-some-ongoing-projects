# 安卓备忘录应用

## 项目概述

本项目旨在开发一个具备用户注册、登录功能的安卓备忘录应用。用户登录后可以管理自己的备忘录，包括添加、修改和删除备忘录。

## 功能需求

1.  **用户模块：**
    *   用户注册
    *   用户登录
    *   登录验证
2.  **备忘录模块：**
    *   以列表形式展示当前用户的备忘录
    *   添加新的备忘录
    *   修改已有的备忘录
    *   删除备忘录

## 技术选型

*   **开发语言：** Java/Kotlin (根据项目实际情况选择，默认为Java)
*   **数据库：** SQLite
*   **IDE：** Android Studio

## 数据库设计

### 1. 用户表 (users)

| 字段名    | 类型    | 约束       | 描述     |
| :-------- | :------ | :--------- | :------- |
| id        | INTEGER | PRIMARY KEY AUTOINCREMENT | 用户ID   |
| username  | TEXT    | UNIQUE NOT NULL | 用户名   |
| password  | TEXT    | NOT NULL   | 密码     |

### 2. 备忘录表 (memos)

| 字段名    | 类型    | 约束       | 描述         |
| :-------- | :------ | :--------- | :----------- |
| id        | INTEGER | PRIMARY KEY AUTOINCREMENT | 备忘录ID     |
| user_id   | INTEGER | NOT NULL   | 用户ID (外键) |
| title     | TEXT    | NOT NULL   | 备忘录标题   |
| content   | TEXT    |            | 备忘录内容   |
| create_time | TEXT  |            | 创建时间     |
| update_time | TEXT  |            | 更新时间     |

## 核心Activity说明

1.  `LoginActivity`: 用户登录界面，处理用户登录逻辑。
2.  `RegisterActivity`: 用户注册界面，处理用户注册逻辑。
3.  `MemoListActivity`: 备忘录列表界面，展示当前用户的备忘录，并提供添加、修改、删除操作的入口。
4.  `AddEditMemoActivity`: 添加/编辑备忘录界面，用于创建新的备忘录或修改已有的备忘录。

## 开发计划 (初步)

1.  搭建项目基本框架。
2.  实现数据库帮助类 (DatabaseHelper) 用于创建表和数据库操作。
3.  实现用户注册功能。
4.  实现用户登录功能。
5.  实现备忘录列表展示功能。
6.  实现添加备忘录功能。
7.  实现修改备忘录功能。
8.  实现删除备忘录功能。
9.  完善UI和用户体验。
10. 测试和Bug修复。

## 如何运行

(待补充)

## 注意事项

*   确保不同用户登录后，只能看到自己的备忘信息。
*   密码存储建议进行加密处理 (例如使用哈希算法)。