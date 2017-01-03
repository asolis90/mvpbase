package com.asolis.mvpexample.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.application.MVPBaseApplication;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.util.SnackbarUtil;

import butterknife.Bind;

/**
 * Created by angelsolis on 11/3/16.
 */

public abstract class BaseActivity<Presenter extends BasePresenter<View>, View extends BaseView> extends AppCompatActivity implements BaseView {
    public abstract Presenter getPresenter();

    @Bind(android.R.id.content)
    protected ContentFrameLayout mRootView;

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

    // ================================
    // ========== View Logic ==========
    // ================================

    @Override
    public void doShowError(String s)
    {
        SnackbarUtil.show(mRootView, s);
    }

    @Override
    public void doShowNetworkError()
    {
        SnackbarUtil.show(mRootView, R.string.network_error);
    }
}
