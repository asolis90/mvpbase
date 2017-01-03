package com.asolis.mvpexample.ui.fingerprintenroll;

import com.asolis.mvpexample.ui.base.BasePresenterImpl;

/**
 * Created by angelsolis on 11/3/16.
 */

public class FingerprintEnrollFragmentPresenterImpl extends BasePresenterImpl<FingerprintEnrollFragmentView> implements FingerprintEnrollFragmentPresenter {
    @Override
    public void onInit() {
        getView().doEnroll_1();
    }
}
