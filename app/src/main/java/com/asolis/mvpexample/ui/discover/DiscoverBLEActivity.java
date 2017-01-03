package com.asolis.mvpexample.ui.discover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.blescan.BaseBLEStartActivity;

import javax.inject.Inject;

public class DiscoverBLEActivity extends BaseBLEStartActivity<DiscoverBLEActivityPresenter, DiscoverBLEActivityView> implements DiscoverBLEActivityView {

    @Inject
    DiscoverBLEActivityPresenter mPresenter;

    public static void launch(Activity callingActivity) {
        Intent intent = new Intent(callingActivity, DiscoverBLEActivity.class);
        callingActivity.startActivity(intent);
    }

    @Override
    public DiscoverBLEActivityPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onBleInitializes() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
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
