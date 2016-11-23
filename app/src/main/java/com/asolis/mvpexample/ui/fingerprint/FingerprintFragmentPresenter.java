package com.asolis.mvpexample.ui.fingerprint;

import com.asolis.mvpexample.ui.base.BasePresenter;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface FingerprintFragmentPresenter extends BasePresenter<FingerprintFragmentView> {
    void onInit();
    void onFetchFingerprints();
}
