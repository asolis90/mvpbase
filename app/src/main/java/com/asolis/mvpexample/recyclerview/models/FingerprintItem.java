package com.asolis.mvpexample.recyclerview.models;

/**
 * Created by angelsolis on 11/28/16.
 */

public class FingerprintItem {
    private String title;
    private String enrollId;

    public FingerprintItem(String title, String enrollId) {
        this.title = title;
        this.enrollId = enrollId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getTitle() {
        return title;
    }
}
