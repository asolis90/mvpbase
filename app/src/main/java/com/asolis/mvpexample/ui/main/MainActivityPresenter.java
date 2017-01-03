package com.asolis.mvpexample.ui.main;

import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.ui.base.BasePresenter;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface MainActivityPresenter extends BasePresenter<MainActivityView> {
    void onInit();
    void onDrawerClicked(DrawerItem item);
}
