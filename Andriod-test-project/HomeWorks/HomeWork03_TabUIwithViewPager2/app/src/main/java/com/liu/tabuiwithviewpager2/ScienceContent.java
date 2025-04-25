package com.liu.tabuiwithviewpager2;

public class ScienceContent {
    private int imageResId;
    private String title;
    private int descriptionResId; // 修改类型为 int

    public ScienceContent(int imageResId, String title, int descriptionResId) { // 修改构造函数参数类型
        this.imageResId = imageResId;
        this.title = title;
        this.descriptionResId = descriptionResId; // 修改赋值
    }

    // Getters
    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public int getDescriptionResId() { return descriptionResId; } // 修改 Getter
}