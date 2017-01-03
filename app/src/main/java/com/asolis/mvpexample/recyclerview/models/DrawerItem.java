package com.asolis.mvpexample.recyclerview.models;

import com.asolis.mvpexample.R;

/**
 * Created by angelsolis on 11/28/16.
 */

public class DrawerItem {
    private String title;
    private int icon = R.drawable.ic_launcher;

    public DrawerItem() {
    }

    public DrawerItem(String title, int resourceId) {
        this.title = title;
        this.icon = resourceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }
}
