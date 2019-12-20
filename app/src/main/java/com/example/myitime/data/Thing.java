package com.example.myitime.data;

public class Thing {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getResourceId() {
        return ResourceId;
    }

    public void setResourceId(int resourceId) {
        ResourceId = resourceId;
    }

    public Thing(String title, String tip, String time, int resourceId) {
        this.title = title;
        this.tip = tip;
        this.time = time;
        ResourceId = resourceId;
    }

    private String title;
    private String tip;
    private String time;
    private int ResourceId;
}
