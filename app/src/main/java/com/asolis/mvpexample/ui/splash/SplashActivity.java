package com.asolis.mvpexample.ui.splash;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseActivity;
import com.asolis.mvpexample.ui.discover.DiscoverBLEActivity;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.util.PreferenceManager;
import com.asolis.mvpexample.util.ServiceUtil;
import com.asolis.mvpexample.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity<SplashActivityPresenter, SplashActivityView> implements SplashActivityView {

    private static final int SPLASH_SECONDS = 2;

    @Inject
    SplashActivityPresenter mPresenter;

    @Override
    public SplashActivityPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void injectSelf(ApplicationComponent component) {
        component.inject(this);
    }

    // ================================
    // ========== View Logic ==========
    // ================================

    @Override
    public void doStartMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.launch(SplashActivity.this);
                finish();
            }
        }, SPLASH_SECONDS * 1000);
    }

    @Override
    public void doStartDiscoverBLEActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DiscoverBLEActivity.launch(SplashActivity.this);
                finish();
            }
        }, SPLASH_SECONDS * 1000);
    }

    @Override
    public void doHandleFailure() {
        SnackbarUtil.showWithAction(mRootView, getString(R.string.network_issue), getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle retry
            }
        });
    }

    @Override
    public void doCheckConnectedDevices() {
        BluetoothManager bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        List<BluetoothDevice> devices = bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        Log.e("devices", "" + devices.size());
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                Log.e("device", "" + "found device");
                if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    if (PreferenceManager.getDeviceAddress(this) != null & PreferenceManager.getDeviceAddress(this).equals(device.getAddress())) {
                        if (ServiceUtil.isMyServiceRunning(this, BLEService.class)) {

                        }
                        break;
                    }
                } else {
                    doStartDiscoverBLEActivity();
                    break;
                }
            }
        } else {
            doStartDiscoverBLEActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceManager.getDeviceAddress(getApplicationContext()) != null) {
            doStartMainActivity();
        } else {
            doStartDiscoverBLEActivity();
        }
    }
}
