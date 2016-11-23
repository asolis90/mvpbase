package com.asolis.mvpexample.ui.base.ble;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEManager2;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.ui.base.BaseActivity;
import com.asolis.mvpexample.ui.base.BasePresenter;
import com.asolis.mvpexample.ui.base.BaseView;
import com.asolis.mvpexample.util.PreferenceManager;

import pl.tajchert.nammu.Nammu;

/**
 * Created by angelsolis on 11/7/16.
 */

public abstract class BaseBLEActivity<Presenter extends BasePresenter<View>, View extends BaseView>
        extends BaseActivity<Presenter, View> implements BaseView {

    private static final String TAG = BaseBLEActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    private static int mTotalActivityCount = 0;
    public BLEService mBluetoothLeService;
    private boolean mConnected;
    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTotalActivityCount++;
        if (checkBluetooth()) {
            doShowConnectingDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTotalActivityCount--;
        // If this is the last activity to shutdown, stop advertising
        if (mTotalActivityCount == 0 && mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    doShowConnectingDialog();
            }
        } else {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    checkBluetooth();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Check if bluetooth is supported
        if (bluetoothAdapter == null) {
            doShowError(getString(R.string.bluetooth_not_found));
            return false;
        } else if (!bluetoothAdapter.isEnabled()) {
            // Check if bluetooth is enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    public class BLEReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BLEService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                // TODO: Show message that we have disconnected from the device..
                Toast.makeText(getApplicationContext(), R.string.disconnected, Toast.LENGTH_SHORT).show();
            } else if (BLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the
                // user interface.
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();
                // TODO: setup main content
            } else if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                onDataAvailable(intent.getStringExtra(BLEService.EXTRA_DATA));
            } else if (BLEService.ACTION_GATT_STOP_SCANNING.equals(action)) {
//                BLEManager2.stopScanning();
            } else if (BLEService.ACTION_DEVICE_NOT_FOUND.equals(action)) {
                onDeviceNotFound();
                // TODO: handle this by either showing a message that device was not found(unable to connect)
                // TODO: show a 'Try to reconnect to my device again', also show a reset button
            } else if (BLEService.ACTION_DEVICE_NULL_ERROR.equals(action)) {
                // TODO: throw them back to the main screen to resart scanning and everything all over again
            }
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BLEService.LocalBinder) service)
                    .getService();
            if (mBluetoothLeService.initialize()) {

                // Automatically connects to the device upon successful start-up
                // initialization.
                mBluetoothLeService.connect(PreferenceManager.getDeviceAddress(getApplicationContext()));
            } else {
                Log.e(TAG, "Unable to initialize Bluetooth");
                // TODO: maybe restart scanning and start all over again.. show a retry screen
                if (getSystemService(Context.BLUETOOTH_SERVICE) == null) {
                    Toast.makeText(getApplicationContext(), R.string.bluetooth_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public void doShowConnectingDialog() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.loading);
        mDialog.setCancelable(false);
        mDialog.show();
        doConnect();
    }

    public void doShowError(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


    public void doConnect() {
        Intent gattServiceIntent = new Intent(this, BLEService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public abstract void onDataAvailable(String data);

    public abstract void onDeviceNotFound();
}
