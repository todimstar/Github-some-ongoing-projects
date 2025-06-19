package com.liu.coursedesign.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "students")
public class Student {
    @PrimaryKey(autoGenerate = true) // 主键自增
    public int id;

    @ColumnInfo(name = "name")
    public String name; // 学生姓名
    @ColumnInfo(name = "age")
    public int age; // 学生年龄

    public Student() {}

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
