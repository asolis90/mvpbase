package com.asolis.mvpexample.ui.splash;

import com.asolis.mvpexample.ui.base.BasePresenterImpl;

/**
 * Created by angelsolis on 11/3/16.
 */

public class SplashActivityPresenterImpl extends BasePresenterImpl<SplashActivityView> implements SplashActivityPresenter {

    @Override
    public void onInit() {
        // TODO: handle maybe login and fetch of data in the future
        getView().doCheckConnectedDevices();
    }
}
