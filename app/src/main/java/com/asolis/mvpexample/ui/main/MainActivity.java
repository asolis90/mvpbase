package com.asolis.mvpexample.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseActivity;

import javax.inject.Inject;

public class MainActivity extends BaseActivity<MainActivityPresenter, MainActivityView> implements MainActivityView {

    @Inject
    MainActivityPresenter mPresenter;

    @Override
    public MainActivityPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPresenter().onInit();
    }

    @Override
    public void injectSelf(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void doShowToast() {
        Toast.makeText(getApplicationContext(), "mvp success", Toast.LENGTH_LONG).show();
    }
}
