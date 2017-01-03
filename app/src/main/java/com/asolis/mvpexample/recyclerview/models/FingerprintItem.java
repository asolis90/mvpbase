package com.asolis.mvpexample.recyclerview.models;

/**
 * Created by angelsolis on 11/28/16.
 */

public class FingerprintItem {
    private String title;
    private int enrollId;

    public FingerprintItem(String title, int enrollId) {
        this.title = title;
        this.enrollId = enrollId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(int enrollId) {
        this.enrollId = enrollId;
    }

    public String getTitle() {
        return title;
    }
}
