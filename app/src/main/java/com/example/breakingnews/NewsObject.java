package com.example.breakingnews;

import android.graphics.Bitmap;

public class NewsObject {

    private String id;
    private String time;
    private String description;
    private String url;

    public NewsObject(String id,String time, String description, String url) {
        this.time = time;
        this.description = description;
        this.url = url;
        this.id = id;
    }

    public String getId() {
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
