package com.asolis.mvpexample.ui.main;

import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.ui.base.BasePresenterImpl;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.home.HomeFragment;
import com.asolis.mvpexample.ui.lights.LightsFragment;

/**
 * Created by angelsolis on 11/3/16.
 */

public class MainActivityPresenterImpl extends BasePresenterImpl<MainActivityView> implements MainActivityPresenter {

    private DrawerItem currentItem;
    @Override
    public void onInit() {
        currentItem = new DrawerItem();
        currentItem.setTitle("Home");
        getView().doShowFragment(HomeFragment.newInstance(), HomeFragment.FRAGMENT_HOME_TAG);
    }

    @Override
    public void onDrawerClicked(DrawerItem item) {
        if(currentItem.getTitle().equals(item.getTitle())) {
            return;
        }

        switch (item.getTitle()) {
            case "Home":
                getView().doShowFragment(HomeFragment.newInstance(), HomeFragment.FRAGMENT_HOME_TAG);
                break;
            case "Lights":
                getView().doShowFragment(LightsFragment.newInstance(), LightsFragment.FRAGMENT_LIGHTS_TAG);
                break;
            case "Fingerprints":
                getView().doShowFragment(FingerprintFragment.newInstance(), FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                break;
        }
    }
}
