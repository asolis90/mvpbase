package com.asolis.mvpexample.ui.fingerprintenroll;

import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.ui.base.BasePresenterImpl;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.home.HomeFragment;
import com.asolis.mvpexample.ui.lights.LightsFragment;

/**
 * Created by angelsolis on 11/3/16.
 */

public class FingerprintEnrollActivityPresenterImpl extends BasePresenterImpl<FingerprintEnrollActivityView> implements FingerprintEnrollActivityPresenter {

    private DrawerItem currentItem;

    @Override
    public void onInit() {
        getView().doSetToolbarTitle("Home");
        getView().doEnroll_1();
    }
}
