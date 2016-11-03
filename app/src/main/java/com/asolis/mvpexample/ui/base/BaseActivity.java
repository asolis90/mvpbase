package com.asolis.mvpexample.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.asolis.mvpexample.application.MVPBaseApplication;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;

/**
 * Created by angelsolis on 11/3/16.
 */

public abstract class BaseActivity<Presenter extends BasePresenter<View>, View extends BaseView> extends AppCompatActivity {
    public abstract Presenter getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectSelf(MVPBaseApplication.get(this).getApplicationComponent());
        getPresenter().setView((View) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().setView(null);
    }

    public abstract void injectSelf(ApplicationComponent component);
}
