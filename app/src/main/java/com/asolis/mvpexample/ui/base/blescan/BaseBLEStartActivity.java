package com.asolis.mvpexample.ui.base.blescan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEManager2;
import com.asolis.mvpexample.ui.base.BaseActivity;
import com.asolis.mvpexample.ui.base.BasePresenter;
import com.asolis.mvpexample.ui.base.BaseView;
import com.asolis.mvpexample.ui.discover.adapter.PagesAdapter;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.util.PermissionsUtil;
import com.asolis.mvpexample.util.PreferenceManager;
import com.asolis.mvpexample.view.BLEViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tajchert.nammu.Nammu;

/**
 * Created by angelsolis on 11/7/16.
 */

public abstract class BaseBLEStartActivity<Presenter extends BasePresenter<View>, View extends BaseView>
        extends BaseActivity<Presenter, View> implements BLEManager2.OnBluetoothManagerListener {
    private static final String TAG = BaseBLEStartActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private static int mTotalActivityCount = 0;
    public BluetoothDevice mBluetoothLeDevice;
    private boolean isScanning = false;
    private BLEManager2 mBLEManager2;
    private CountDownTimer mTimer = new CountDownTimer(5000, 1000) {

        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            if (mBLEManager2 != null) {
                mBLEManager2.stopScanning();
                Toast.makeText(getApplicationContext(), "Not able to find a Device..", Toast.LENGTH_SHORT).show();
                isScanning = false;
                mViewPager.setCurrentItem(0);
            }
        }
    };

    @Bind(R.id.pager)
    public BLEViewPager mViewPager;

    protected abstract void onBleInitializes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTotalActivityCount++;
        checkBluetooth();
        initializeBLEManager();
        if (savedInstanceState != null) {
            isScanning = savedInstanceState.getBoolean("scanning");
        }
    }

    private void initializeBLEManager() {
        mBLEManager2 = new BLEManager2(this);
        mBLEManager2.setOnDeviceFoundListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // Find our views
        ButterKnife.bind(this);
        mViewPager.setAdapter(new PagesAdapter(this, mCallback));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(0);
        mViewPager.setPagingEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("scanning", isScanning);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTotalActivityCount--;
        // If this is the last activity to shutdown, stop scanning
        if (mTotalActivityCount == 0) {
            if (isScanning && mViewPager.getCurrentItem() == 1 && checkBluetooth()) {
                PermissionsUtil.checkLocationPermission(this, getString(R.string.error_location_permissions),
                        new PermissionsUtil.PermissionUtilCallback() {
                            @Override
                            public void onResult(boolean result) {
                                if (result) {
                                    Log.e("onResult - onPause", "here");
                                    isScanning = false;
                                    if (mBLEManager2 != null) {
                                        mTimer.cancel();
                                        mBLEManager2.stopScanning();
                                    }
                                }
                            }
                        });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager.getCurrentItem() == 1) {
            if (checkBluetooth()) {
                Log.e("onResume", "here");
                mTimer.start();
                startBLEScan();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    if (mViewPager.getCurrentItem() == 1 && checkBluetooth()) {
                        startBLEScan();
                    }
            }
        } else {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    checkBluetooth();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isScanning && mViewPager.getCurrentItem() == 1 && checkBluetooth()) {
            PermissionsUtil.checkLocationPermission(this, getString(R.string.error_location_permissions),
                    new PermissionsUtil.PermissionUtilCallback() {
                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                Log.e("onResult - onPause", "here");
                                isScanning = false;
                                if (mBLEManager2 != null) {
                                    mTimer.cancel();
                                    mBLEManager2.stopScanning();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startBLEScan() {
        // If this is the first activity, start advertising
        if (mTotalActivityCount == 1) {
            isScanning = true;
            if (mBLEManager2 == null) {
                mBLEManager2 = null;
                initializeBLEManager();
            }
            mTimer.start();
            mBLEManager2.scanLeDevice();
            onBleInitializes();
        }
    }

    private boolean checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Check if bluetooth is supported
        if (bluetoothAdapter == null) {
            doShowError(getString(R.string.bluetooth_not_found));
            return false;
        } else if (!bluetoothAdapter.isEnabled()) {
            // Check if bluetooth is enabled
            requestEnablingBluetooth();
            return false;
        }
        return true;
    }

    private void requestEnablingBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        // TODO: save device address into shared preferences to be used in splash activity
        mBluetoothLeDevice = device;
        PreferenceManager.setDeviceAddress(getApplicationContext(), device.getAddress());
        // TODO: start main activity
        MainActivity.launch(this);
    }

    @Override
    public void onScanPeriodEnd(BluetoothDevice device) {
        if (!isFinishing()) {
            if (device == null) {
                Toast.makeText(this, "Not able to find a Device..", Toast.LENGTH_SHORT).show();
                isScanning = false;
                mViewPager.setCurrentItem(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            finish();
        } else {
            if (isScanning && mViewPager.getCurrentItem() == 1 && checkBluetooth()) {
                PermissionsUtil.checkLocationPermission(this, getString(R.string.error_location_permissions),
                        new PermissionsUtil.PermissionUtilCallback() {
                            @Override
                            public void onResult(boolean result) {
                                if (result) {
                                    isScanning = false;
                                    if (mBLEManager2 != null) {
                                        mBLEManager2.stopScanning();
                                    }
                                }
                            }
                        });
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    public interface PagerCallback {
        void onClick(android.view.View v);
    }

    public PagerCallback mCallback = new PagerCallback() {
        @Override
        public void onClick(android.view.View v) {
            Log.e("item before", "" + mViewPager.getCurrentItem());
            mViewPager.setCurrentItem(1);
            Log.e("item after", "" + mViewPager.getCurrentItem());
            PermissionsUtil.checkLocationPermission(BaseBLEStartActivity.this,
                    getString(R.string.error_location_permissions),
                    new PermissionsUtil.PermissionUtilCallback() {
                        @Override
                        public void onResult(boolean result) {
                            if (result) {
                                startBLEScan();
                            }
                        }
                    });
        }
    };
}
