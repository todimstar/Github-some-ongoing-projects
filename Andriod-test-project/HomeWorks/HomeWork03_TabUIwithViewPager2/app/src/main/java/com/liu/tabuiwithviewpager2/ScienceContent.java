// 在你的项目包下创建一个新的 Java 类, 例如 ScienceContent.java
package com.liu.tabuiwithviewpager2;

public class ScienceContent {
    private int imageResId;
    private String title;
    private String description;

    public ScienceContent(int imageResId, String title, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
    }

    // Getters
    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}