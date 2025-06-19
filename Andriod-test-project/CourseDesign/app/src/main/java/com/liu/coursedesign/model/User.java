package com.liu.coursedesign.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)    // 主键自增
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "role")
    public String role; // 角色：管理员、普通用户等

    public User(){}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}



