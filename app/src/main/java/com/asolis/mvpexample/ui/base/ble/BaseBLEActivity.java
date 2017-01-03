package com.asolis.mvpexample.ui.base.ble;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.AdvertiserService;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.ble.IRemoteAdvertiseService;
import com.asolis.mvpexample.ble.IRemoteInterfaceCallback;
import com.asolis.mvpexample.ble.IRemoteInterfaceService;
import com.asolis.mvpexample.ui.base.BaseActivity;
import com.asolis.mvpexample.ui.base.BasePresenter;
import com.asolis.mvpexample.ui.base.BaseView;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.home.HomeFragment;
import com.asolis.mvpexample.util.PreferenceManager;
import com.asolis.mvpexample.util.ServiceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tajchert.nammu.Nammu;

/**
 * Created by angelsolis on 11/7/16.
 */

public abstract class BaseBLEActivity<Presenter extends BasePresenter<View>, View extends BaseView>
        extends BaseActivity<Presenter, View> implements BaseView {

    private static final String TAG = BaseBLEActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_FINGERPRINT_ENROLL = 2;
    public BLEService mBluetoothLeService;
    public static IRemoteInterfaceService mRemoteService;
    //    private static AdvertiserService mAdvertiseService;
    private boolean mConnected;
    private Dialog mDialog;
//    private boolean isAdvertiseServiceBinded = false;

    @Bind(R.id.toolbar)
    public
    Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkBluetooth();
//            doShowConnectingDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume - base", "here");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (ServiceUtil.isMyServiceRunning(this, BLEService.class)) {
                Intent gattServiceIntent = new Intent(this, BLEService.class);
                bindService(gattServiceIntent, mServiceConnection, 0);
            } else {
                Intent gattServiceIntent = new Intent(this, BLEService.class);
                if (PreferenceManager.getDeviceAddress(getApplicationContext()) != null) {
                    startService(gattServiceIntent);
                    bindService(gattServiceIntent, mServiceConnection, 0);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(bleReceiver, intentFilter());
    }

    public void startAdvertisingService() {
        if (mRemoteService != null) {
            try {
                mRemoteService.startAdvertiseService();
                mRemoteService.onInit();
                PreferenceManager.setDisconnectManually(getApplicationContext(), false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("mRemoteService", "is null");
        }
    }

//    private final ServiceConnection mAdvertiseServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            Log.e("mAdvertiseServiceConnec", " connected here");
//
//            AdvertiserService.LocalBinder binder = (AdvertiserService.LocalBinder) service;
//            mAdvertiseService = binder.getService();
//            mAdvertiseService.onInit();
//            isAdvertiseServiceBinded = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mAdvertiseService = null;
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bleReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
//        if (mAdvertiseService != null) {
//            if (ServiceUtil.isMyServiceRunning(this, AdvertiserService.class)) {
//                if (isAdvertiseServiceBinded) {
//                    Log.e("unbind - adv service", "here");
//                    unbindService(mAdvertiseServiceConnection);
//                    isAdvertiseServiceBinded = false;
//                }
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","here");
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    doShowConnectingDialog();
                    break;
                case REQUEST_FINGERPRINT_ENROLL:
                    Log.e(TAG, "REQUEST_FINGERPRINT_ENROLL - here");
                    boolean resp = data.getBooleanExtra("response", false);
                    Log.e(TAG, "resp" + resp);
                    if(resp) {
                        FingerprintFragment fingerprintFragment = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                        if (fingerprintFragment != null) {

                            // clear all the fingerprints
                            fingerprintFragment.doFetchFingerprints();
                        }
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    checkBluetooth();
            }
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initToolbar();
    }

    public void initToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(getResources().getInteger(R.integer.action_bar_elevation));
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(false);
            prepareToolbar(actionBar);
        }
    }

    public abstract void prepareToolbar(ActionBar actionBar);

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

    private final BroadcastReceiver bleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BLEService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BLEService.ACTION_GATT_DISCONNECTED.equals(action)) {
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG) instanceof HomeFragment) {
                    Log.e("ACTION_GATT_DISCONN", " here");
                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG)).setStatusDisconnected();
                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG)).refreshMenu();
                }
                mConnected = false;
                // TODO: Show message that we have disconnected from the device..
                Toast.makeText(getApplicationContext(), R.string.disconnected, Toast.LENGTH_SHORT).show();
            } else if (BLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.e(TAG, "ACTION_GATT_SERVICES_DISCOVERED - here");
//                if (isAdvertiseServiceBinded) {
//                    Log.e("unbind - adv service", "here");
//                    unbindService(mAdvertiseServiceConnection);
//                    isAdvertiseServiceBinded = false;
//                }
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG) instanceof HomeFragment) {
                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG)).setStatusConnected();
                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG)).refreshMenu();
                }
                Log.e("discovered", "action: here");
//                if(mDialog != null) {
//                    mDialog.dismiss();
//                }
                Toast.makeText(getApplicationContext(), R.string.connected, Toast.LENGTH_SHORT).show();
                // TODO: setup main content
//                if (ServiceUtil.isMyServiceRunning(getApplicationContext(), BLEService.class)) {
//                    Intent gattServiceIntent = new Intent(getApplicationContext(), BLEService.class);
//                    bindService(gattServiceIntent, mServiceConnection, 0);
//                } else {
//                    doConnect();
//                }
            } else if (BLEService.ACTION_GATT_ALREADY_CONNECTED.equals(action)) {
                Log.e("ACTION_GATT_ALREADYCONN", "here");
                if (getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG) instanceof HomeFragment) {
                    Log.e("instance of ", " home");
                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.FRAGMENT_HOME_TAG)).setStatusConnected();
                }
                mDialog.dismiss();
            } else if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.e(TAG, "ACTION_DATA_AVAILABLE - here");
                onDataAvailable(intent.getByteArrayExtra(BLEService.EXTRA_DATA));
            } else if (BLEService.ACTION_DEVICE_NOT_FOUND.equals(action)) {
                onDeviceNotFound();
                // TODO: handle this by either showing a message that device was not found(unable to connect)
                // TODO: show a 'Try to reconnect to my device again', also show a reset button
            } else if (BLEService.ACTION_DEVICE_RETRY_NOT_FOUND.equals(action)) {
                Toast.makeText(getApplicationContext(), R.string.reconnect_error, Toast.LENGTH_SHORT).show();
            } else if (BLEService.ACTION_DEVICE_NULL_ERROR.equals(action)) {
                // TODO: throw them back to the main screen to resart scanning and everything all over again
            } else if (BLEService.ACTION_REQUEST_BLUETOOTH_PERMISSION.equals(action)) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else if (BLEService.ACTION_DEVICE_WITHOUT_BLUETOOTH.equals(action)) {
                Toast.makeText(getApplicationContext(), R.string.bluetooth_not_found, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private static IntentFilter intentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BLEService.ACTION_DEVICE_NOT_FOUND);
        intentFilter.addAction(BLEService.ACTION_DEVICE_NULL_ERROR);
        intentFilter.addAction(BLEService.ACTION_GATT_STOP_SCANNING);
        intentFilter.addAction(BLEService.ACTION_GATT_ALREADY_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_DEVICE_RETRY_NOT_FOUND);
        return intentFilter;
    }

//    private CountDownTimer mConnectionTimer = new CountDownTimer(2000, 1000) {
//
//        public void onTick(long millisUntilFinished) {
//        }
//
//        public void onFinish() {
//            if (mRemoteService != null) {
//                try {
//                    if (!mRemoteService.isDeviceConnected()) {
//                        Log.e("timer restart", "here");
//                        mRemoteService.onInit();
//                        mConnectionTimer.start();
//                    }
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mRemoteService = IRemoteInterfaceService.Stub.asInterface((IBinder) service);
            try {
                if (!mRemoteService.isDeviceConnected()) {
                    Log.e("mServiceConnection-onServiceConn", "here");
                    if (!PreferenceManager.isDisconnectedManually(getApplicationContext())) {
                        mRemoteService.onInit();
                    }
                }
                mRemoteService.registerCallback(new IRemoteInterfaceCallback.Stub() {
                    @Override
                    public void test() throws RemoteException {

                    }

                });
            } catch (
                    RemoteException e
                    )

            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteService = null;
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
        startService(gattServiceIntent);
        bindService(gattServiceIntent, mServiceConnection, 0);
    }

    public abstract void onDataAvailable(byte[] data);

    public abstract void onDeviceNotFound();
}
