package com.asolis.mvpexample.ui.fingerprint;

import com.asolis.mvpexample.ui.base.BaseView;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface FingerprintFragmentView extends BaseView {
    void doShowToast();
    void doFetchFingerprints();
}
