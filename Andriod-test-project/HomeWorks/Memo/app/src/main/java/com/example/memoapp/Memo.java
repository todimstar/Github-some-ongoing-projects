package com.example.memoapp;

public class Memo {
    private int id;
    private int userId;
    private String title;
    private String content;
    private String createTime;
    private String updateTime;

    // 构造函数
    public Memo() {
    }

    public Memo(int id, int userId, String title, String content, String createTime, String updateTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Memo(int userId, String title, String content, String createTime, String updateTime) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        // ListView中默认会调用toString()方法显示对象，这里可以简单返回标题
        return title;
    }
}