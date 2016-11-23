package com.asolis.mvpexample.ui.base.blescan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEManager2;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.ui.base.BaseActivity;
import com.asolis.mvpexample.ui.base.BasePresenter;
import com.asolis.mvpexample.ui.base.BaseView;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.util.PreferenceManager;

import pl.tajchert.nammu.Nammu;

/**
 * Created by angelsolis on 11/7/16.
 */

public abstract class BaseBLEScanActivity<Presenter extends BasePresenter<View>, View extends BaseView>
        extends BaseActivity<Presenter, View> implements BLEManager2.OnBluetoothManagerListener {
    private static final String TAG = BaseBLEScanActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private static int mTotalActivityCount = 0;
    private BluetoothGatt mGatt;
    public BLEService mBluetoothLeService;
    public BluetoothDevice mBluetoothLeDevice;
    private boolean mConnected;

    protected abstract void onBleInitializes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTotalActivityCount++;
        enableBluetooth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTotalActivityCount--;
        // If this is the last activity to shutdown, stop advertising
        if (mTotalActivityCount == 0) {
            if (mGatt == null) {
                return;
            }
            mGatt.disconnect();
            mGatt.close();
            mGatt = null;
//            BLEManager2.stopScanning();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            startBLEAdvertising();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
//            BLEManager2.stopScanning();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    startBLEAdvertising();
            }
        } else {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    enableBluetooth();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Check if bluetooth is supported
        if (bluetoothAdapter == null) {
            doShowError(getString(R.string.bluetooth_not_found));
        } else {
            // Check if bluetooth is enabled
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                startBLEAdvertising();
            }
        }
    }

    private void startBLEAdvertising() {
        // If this is the first activity, start advertising
        if (mTotalActivityCount == 1) {
            BLEManager2 mBLEManager2 = new BLEManager2(this);
            mBLEManager2.setOnDeviceFoundListener(this);
            mBLEManager2.scanLeDevice();
            onBleInitializes();
        }
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        // TODO: save device address into shared preferences to be used in splash activity
        mBluetoothLeDevice = device;
        PreferenceManager.setDeviceAddress(getApplicationContext(), device.getAddress());
        // TODO: start main activity
        MainActivity.launch(this);
    }
}
