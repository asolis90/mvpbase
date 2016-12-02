package com.asolis.mvpexample.ui.main;



import android.support.v4.app.Fragment;

import com.asolis.mvpexample.ui.base.BaseView;

/**
 * Created by angelsolis on 11/3/16.
 */

public interface MainActivityView extends BaseView {
    void doShowToast();
    void doShowFragmentWithStack(Fragment fragment, String TAG);
    void doShowFragmentWithoutStack(Fragment fragment, String TAG);
    void doSetToolbarTitle(String title);
}
