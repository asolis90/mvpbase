package com.asolis.mvpexample.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by angelsolis on 11/6/16.
 */

@TargetApi(21)
public class BLEManager {
    private static final String COMPANY_UUID = "70212055-3409-8943-1500-4061e0b5495e";
    private static BluetoothLeScanner mLEScanner;
    private static BluetoothAdapter mBluetoothAdapter;
    private static ScanSettings settings;
    private static final int PAYLOAD_START_INDEX = 14;
    private static final int PAYLOAD_END_INDEX = PAYLOAD_START_INDEX + 16;
    private static final String TAG = BLEManager.class.getSimpleName();
    private static BluetoothDevice mCurrentBtDevice;
    private static boolean stoppedScanning = false;
    private static boolean isDeviceFound = false;
    @NonNull
    private Context mContext;

    private BLEManager() {
        // Do Nothing..
    }

    public BLEManager(@NonNull Context context) {
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
            Log.e("scanning","here");
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
//            StringBuilder sb = new StringBuilder();
//            for (int i = PAYLOAD_START_INDEX; i < PAYLOAD_END_INDEX; i++) {
//                sb.append(String.format("%02X", result.getScanRecord().getBytes()[i]));
//            }

            List<ParcelUuid> serviceUuids = result.getScanRecord().getServiceUuids();
            String uuid = "";
            if(serviceUuids != null && serviceUuids.size() > 0) {
                uuid = serviceUuids.get(0).toString();
            }
            if (uuid.startsWith(COMPANY_UUID)) {
                Log.e("COMPANY UUID", "yes");
                Log.e("result", result.toString());
                if(!isDeviceFound) {
                    isDeviceFound = true;
                    final BluetoothDevice bluetoothDevice = result.getDevice();
                    mCurrentBtDevice = bluetoothDevice;
                    mOnBluetoothManagerListener.onDeviceFound(bluetoothDevice);
                }

            } else {
                Log.e("callbackType", String.valueOf(callbackType));
                Log.e("result", result.toString());
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

    public BLEManager setOnDeviceFoundListener(OnBluetoothManagerListener callback) {
        mOnBluetoothManagerListener = callback;
        return this;
    }

    public interface OnBluetoothManagerListener {
        void onDeviceFound(BluetoothDevice device);

        void onScanPeriodEnd(BluetoothDevice device);
    }
}
