package com.asolis.mvpexample.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by angelsolis on 11/3/16.
 */

public abstract class BaseActivity<Presenter extends BasePresenter<View>, View extends BaseView> extends AppCompatActivity {
    public abstract Presenter getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().setView((View) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().setView(null);
    }
}
