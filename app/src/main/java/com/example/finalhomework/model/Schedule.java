package com.example.finalhomework.model;

public class Schedule {
    private int id;
    private String title;
    private String duration;
    private String content;

    public Schedule(int id, String title, String duration, String content) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getContent() {
        return content;
    }
}