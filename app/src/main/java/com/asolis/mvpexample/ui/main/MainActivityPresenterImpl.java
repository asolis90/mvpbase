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
        getView().doShowFragmentWithoutStack(HomeFragment.newInstance(), HomeFragment.FRAGMENT_HOME_TAG);
        getView().doSetToolbarTitle("Home");
    }

    @Override
    public void onDrawerClicked(DrawerItem item) {
        if (currentItem.getTitle().equals(item.getTitle())) {
            return;
        }
        currentItem = item;
        switch (item.getTitle()) {
            case "Home":
                getView().doShowFragmentWithoutStack(HomeFragment.newInstance(), HomeFragment.FRAGMENT_HOME_TAG);
                getView().doSetToolbarTitle("Home");
                break;
            case "Lights":
                getView().doShowFragmentWithoutStack(LightsFragment.newInstance(), LightsFragment.FRAGMENT_LIGHTS_TAG);
                getView().doSetToolbarTitle("Lights");
                break;
            case "Fingerprints":
                getView().doShowFragmentWithoutStack(FingerprintFragment.newInstance(), FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                getView().doSetToolbarTitle("Fingerprints");
                break;
        }
    }
}
