package com.asolis.mvpexample.ui.splash;

import com.asolis.mvpexample.ui.base.BaseView;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface SplashActivityView extends BaseView {
    void doStartMainActivity();
    void doStartDiscoverBLEActivity();
    void doHandleFailure();
    void doCheckConnectedDevices();
}
