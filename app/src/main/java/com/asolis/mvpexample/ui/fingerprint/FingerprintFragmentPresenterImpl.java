package com.asolis.mvpexample.ui.fingerprint;

import com.asolis.mvpexample.ui.base.BasePresenterImpl;

/**
 * Created by angelsolis on 11/3/16.
 */

public class FingerprintFragmentPresenterImpl extends BasePresenterImpl<FingerprintFragmentView> implements FingerprintFragmentPresenter {
    @Override
    public void onInit() {
    }

    @Override
    public void onFetchFingerprints() {
        getView().doFetchFingerprints();
    }

}
