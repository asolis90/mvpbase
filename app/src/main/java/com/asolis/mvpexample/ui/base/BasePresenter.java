package com.asolis.mvpexample.ui.base;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface BasePresenter<View extends BaseView> {
    void setView(View view);

    View getView();
}
