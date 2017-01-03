package com.asolis.mvpexample.ui.fingerprintenroll;

import com.asolis.mvpexample.ui.base.BaseView;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface FingerprintEnrollFragmentView extends BaseView {
    void doShowToast();
    void doEnroll_1();
    void doEnroll_2();
    void doEnroll_3();
    void doUpdateUI(String type);
}
