package com.asolis.mvpexample.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by angelsolis on 11/6/16.
 */

@TargetApi(21)
public class BLEManager2 {
    private static final long SCAN_PERIOD = 10000;
    private static final String COMPANY_UUID = "5E49B5E061";
    private static BluetoothLeScanner mLEScanner;
    private static BluetoothAdapter mBluetoothAdapter;
    private static ScanSettings settings;
    private static List<ScanFilter> filters;
    private static final int PAYLOAD_START_INDEX = 14;
    private static final int PAYLOAD_END_INDEX = PAYLOAD_START_INDEX + 16;
    private static final String TAG = BLEManager2.class.getSimpleName();
    private static BluetoothDevice mCurrentBtDevice;
    private static boolean stoppedScanning = false;
    @NonNull
    private Context mContext;

    private BLEManager2() {
        // Do Nothing..
    }

    public BLEManager2(@NonNull Context context) {
        mContext = context;
    }

    private static OnBluetoothManagerListener mOnBluetoothManagerListener = new OnBluetoothManagerListener() {
        @Override
        public void onDeviceFound(BluetoothDevice device) {
            // Do Nothing..
        }

        @Override
        public void onScanPeriodEnd(BluetoothDevice device) {
            // Do Nothing..
        }
    };

    public void scanLeDevice() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT >= 21) {
            if (mLEScanner == null) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                        .build();
            }
            mLEScanner.startScan(null, settings, mScanCallback);
        }
    }

    private static ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if (result.getScanRecord() == null || result.getScanRecord().getDeviceName() == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = PAYLOAD_START_INDEX; i < PAYLOAD_END_INDEX; i++) {
                sb.append(String.format("%02X", result.getScanRecord().getBytes()[i]));
            }
            if (sb.toString().startsWith(COMPANY_UUID)) {
                Log.e("COMPANY UUID", "yes");
                final BluetoothDevice bluetoothDevice = result.getDevice();
                mCurrentBtDevice = bluetoothDevice;
                mOnBluetoothManagerListener.onDeviceFound(bluetoothDevice);

            } else {
                Log.e("callbackType", String.valueOf(callbackType));
                Log.e("result", result.toString());
                Log.e("sb", "" + sb.toString());
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    public void stopScanning() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            stoppedScanning = true;
            Log.e("stopScanning", "here");
            mLEScanner.stopScan(mScanCallback);
        }
    }

    public BLEManager2 setOnDeviceFoundListener(OnBluetoothManagerListener callback) {
        mOnBluetoothManagerListener = callback;
        return this;
    }

    public interface OnBluetoothManagerListener {
        void onDeviceFound(BluetoothDevice device);

        void onScanPeriodEnd(BluetoothDevice device);
    }
}
