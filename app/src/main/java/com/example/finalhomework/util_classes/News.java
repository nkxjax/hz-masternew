package com.example.finalhomework.util_classes;

import androidx.annotation.NonNull;

public class News {
    private int id;
    private String title;
    private String content;
    private String publishTime;

    // 构造函数
    public News(String title, String content, String publishTime) {
        this.title = title;
        this.content = content;
        this.publishTime = publishTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPublishTime() { return publishTime; }

    public void setPublishTime(String publishTime) { this.publishTime = publishTime; }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}

