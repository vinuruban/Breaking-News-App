package com.example.breakingnews;

public class NewsObject {

    private long id;
    private String time;
    private String description;
    private String url;

    public NewsObject(long id,String time, String description, String url) {
        this.id = id;
        this.time = time;
        this.description = description;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
