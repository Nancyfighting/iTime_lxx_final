package com.example.myitime.data;

import java.io.Serializable;

public class Thing implements Serializable {

    private String title;
    private String tip;
    private int[] time;
    private byte[] image;
    private String auto_pre;


    public Thing(String title, String tip, int[] time, byte[] image, String auto_pre) {
        this.title = title;
        this.tip = tip;
        this.time = time;
        this.image = image;
        this.auto_pre = auto_pre;
    }

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

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAuto_pre() {
        return auto_pre;
    }

    public void setAuto_pre(String auto_pre) {
        this.auto_pre = auto_pre;
    }
}
