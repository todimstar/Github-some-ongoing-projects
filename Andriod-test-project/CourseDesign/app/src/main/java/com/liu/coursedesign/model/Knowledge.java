package com.liu.coursedesign.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "knowledges")
public class Knowledge {
    @PrimaryKey(autoGenerate = true) // 主键自增
    public int id;

    @ColumnInfo(name = "title")
    public String title; // 标题

    @ColumnInfo(name = "description")
    public String description; // 描述

    @ColumnInfo(name = "imagePath")
    public String imagePath; // 图片路径

//    @ColumnInfo(name = "keyName")
//    public String keyName; // 关键物品名，目前还不确定选择讲解什么

    @ColumnInfo(name = "category")
    public String category; // 知识分类，例如：循环、条件语句、函数等

    @ColumnInfo(name = "createTime")
    public String createTime; // 创建时间

    @ColumnInfo(name = "updateTime")
    public String updateTime; // 更新时间

    public Knowledge() {}

    public Knowledge(String title, String description, String imagePath, String category, String createTime, String updateTime) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.category = category;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Knowledge{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", category='" + category + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public int getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getCategory() {
        return category;
    }
    public String getCreateTime() {
        return createTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
