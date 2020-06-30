package com.example.breakingnews;

import android.graphics.Bitmap;

public class NewsObject {

    private String time;
    private String description;

    public NewsObject(String time, String description) {
        this.time = time;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
