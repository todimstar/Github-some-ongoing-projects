package com.example.memoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DATABASE_NAME = "memo_app.db";
    // 数据库版本号
    private static final int DATABASE_VERSION = 1;

    // 用户表
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // 备忘录表
    public static final String TABLE_MEMOS = "memos";
    public static final String COLUMN_MEMO_ID = "id";
    public static final String COLUMN_MEMO_USER_ID = "user_id"; // 外键，关联用户表
    public static final String COLUMN_MEMO_TITLE = "title";
    public static final String COLUMN_MEMO_CONTENT = "content";
    public static final String COLUMN_MEMO_CREATE_TIME = "create_time";
    public static final String COLUMN_MEMO_UPDATE_TIME = "update_time";

    // 创建用户表的SQL语句
    private static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USERNAME + " TEXT UNIQUE NOT NULL," +
            COLUMN_PASSWORD + " TEXT NOT NULL)";

    // 创建备忘录表的SQL语句
    private static final String SQL_CREATE_TABLE_MEMOS = "CREATE TABLE " + TABLE_MEMOS + " (" +
            COLUMN_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_MEMO_USER_ID + " INTEGER NOT NULL," +
            COLUMN_MEMO_TITLE + " TEXT NOT NULL," +
            COLUMN_MEMO_CONTENT + " TEXT," +
            COLUMN_MEMO_CREATE_TIME + " TEXT," +
            COLUMN_MEMO_UPDATE_TIME + " TEXT," +
            "FOREIGN KEY(" + COLUMN_MEMO_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        db.execSQL(SQL_CREATE_TABLE_USERS);
        // 创建备忘录表
        db.execSQL(SQL_CREATE_TABLE_MEMOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级逻辑，如果需要的话
        // 例如：删除旧表，然后重新创建
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // 检查用户名是否存在
    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        android.database.Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        // db.close(); // Reading database, no need to close here, will be closed by the caller or when activity finishes
        return count > 0;
    }

    // 验证用户登录
    public User checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD /* 通常不直接查询密码字段用于验证 */};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        android.database.Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            // 在实际应用中，这里应该比较哈希后的密码
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            if (password.equals(storedPassword)) { // 简单比较，实际应使用安全比较方式
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
                // user.setPassword(storedPassword); // 通常不将密码设置回User对象传递
            }
        }
        cursor.close();
        // db.close(); // Reading database, no need to close here
        return user;
    }

    // 验证用户登录 (此版本检查密码，之前的 checkUserCredentials 仅检查用户名是否存在并返回User对象，但密码比较逻辑在方法内)
    // 为了清晰，可以将上面的 checkUserCredentials(String username, String password) 方法保留用于登录验证
    // 而 checkUser(String username) 仅用于检查用户名是否存在。
    // 这里保留了原始的第二个 checkUserCredentials，它同时检查用户名和密码，并返回User对象
    // 如果需要一个只检查用户名是否存在并返回boolean的方法，应该使用上面的 public boolean checkUser(String username)
    public User checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password}; // 注意：实际项目中密码比较前也应加密处理
        android.database.Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)));
            // user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))); // 通常不直接返回密码
        }
        cursor.close();
        // db.close(); // Reading database, no need to close here
        return user;
    }
}