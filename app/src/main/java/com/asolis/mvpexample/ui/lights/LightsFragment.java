package com.asolis.mvpexample.ui.lights;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.ble.util.CharacteristicHelper;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseFragment;
import com.asolis.mvpexample.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.OnClick;

public class LightsFragment extends BaseFragment<LightsFragmentPresenter, LightsFragmentView> implements LightsFragmentView {

    @Inject
    LightsFragmentPresenter mPresenter;

    public static String FRAGMENT_LIGHTS_TAG = "lights";

    public static LightsFragment newInstance() {
        return new LightsFragment();
    }

    @Override
    public LightsFragmentPresenter getPresenter() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lights, container, false);
        return view;
    }

    @Override
    public void injectSelf(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void doShowToast() {
    }

    @Override
    public void doShowError(String s) {

    }

    @Override
    public void doShowNetworkError() {

    }
}
