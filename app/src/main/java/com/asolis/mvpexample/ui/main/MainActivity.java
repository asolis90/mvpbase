package com.asolis.mvpexample.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainActivityPresenter, MainActivityView> implements MainActivityView {

    MainActivityPresenter mPresenter;

    @Override
    public MainActivityPresenter getPresenter() {
        mPresenter = new MainActivityPresenterImpl();
        mPresenter.setView(this);
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPresenter().onInit();
    }

    @Override
    public void doShowToast() {
        Toast.makeText(getApplicationContext(), "mvp success", Toast.LENGTH_LONG).show();
    }
}
