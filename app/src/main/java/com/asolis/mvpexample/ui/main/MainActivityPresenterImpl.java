package com.asolis.mvpexample.ui.main;

import com.asolis.mvpexample.ui.base.BasePresenterImpl;

/**
 * Created by angelsolis on 11/3/16.
 */

public class MainActivityPresenterImpl extends BasePresenterImpl<MainActivityView> implements MainActivityPresenter {

    @Override
    public void onInit() {
        getView().doShowToast();
    }
}
