package com.asolis.mvpexample.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.asolis.mvpexample.ble.util.CharacteristicHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created by angelsolis on 12/12/16.
 */

/**
 * Manages BLE Advertising independent of the main app.
 * If the app goes off screen (or gets killed completely) advertising can continue because this
 * Service is maintaining the necessary Callback in memory.
 */
public class AdvertiserService extends Service {

    private static final String TAG = AdvertiserService.class.getSimpleName();

    public final static String ACTION_DEVICE_WITHOUT_BLUETOOTH =
            "com.asolis.mvpexample.ACTION_DEVICE_WITHOUT_BLUETOOTH";
    public final static String ACTION_REQUEST_BLUETOOTH_PERMISSION =
            "com.asolis.mvpexample.ACTION_REQUEST_BLUETOOTH_PERMISSION";

    /**
     * A global variable to let AdvertiserFragment check if the Service is running without needing
     * to start or bind to it.
     * This is the best practice method as defined here:
     * https://groups.google.com/forum/#!topic/android-developers/jEvXMWgbgzE
     */
    public static boolean running = false;

    public static final String ADVERTISING_FAILED =
            "com.example.android.bluetoothadvertisements.advertising_failed";

    public static final String ADVERTISING_FAILED_EXTRA_CODE = "failureCode";

    public static final int ADVERTISING_TIMED_OUT = 6;

    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    private AdvertiseCallback mAdvertiseCallback;

    private Handler mHandler;

    private Runnable timeoutRunnable;

    /**
     * Length of time to allow advertising before automatically shutting off. (10 minutes)
     */
    private long TIMEOUT = TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS);

    @Override
    public void onDestroy() {
        Log.e("onDestroy","here");
        /**
         * Note that onDestroy is not guaranteed to be called quickly or at all. Services exist at
         * the whim of the system, and onDestroy can be delayed or skipped entirely if memory need
         * is critical.
         */
        running = false;
        stopAdvertising_();
//        mHandler.removeCallbacks(timeoutRunnable);
        super.onDestroy();
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     **/
    public boolean initialize() {
        if (mBluetoothLeAdvertiser == null) {
            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager != null) {
                BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();
                if (mBluetoothAdapter != null) {
                    mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
                } else {
                    Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
                    return false;
                }
            } else {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        return true;
    }

    /**
     * Starts a delayed Runnable that will cause the BLE Advertising to timeout and stop after a
     * set amount of time.
     */
    public void setTimeout() {
        mHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "AdvertiserService has reached timeout of " + TIMEOUT + " milliseconds, stopping advertising.");
                sendFailureIntent(ADVERTISING_TIMED_OUT);
                stopSelf();
            }
        };
        mHandler.postDelayed(timeoutRunnable, TIMEOUT);
    }

    /**
     * Starts BLE Advertising.
     */
    public void startAdvertising() {
        running = true;
        Log.e(TAG, "here -  Starting Advertising");

        AdvertiseSettings settings = buildAdvertiseSettings();
        AdvertiseData data = buildAdvertiseData();
        Log.e("data", data.toString());
        if (mAdvertiseCallback == null) {
            mAdvertiseCallback = new SampleAdvertiseCallback();
        }
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.startAdvertising(settings, data,
                    mAdvertiseCallback);
        }

    }

    /**
     * Stops BLE Advertising.
     */
    private void stopAdvertising_() {
        Log.d(TAG, "Service: Stopping Advertising");
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
    }

    /**
     * Returns an AdvertiseData object which includes the Service UUID and Device Name.
     */
    private AdvertiseData buildAdvertiseData() {

        /**
         * Note: There is a strict limit of 31 Bytes on packets sent over BLE Advertisements.
         *  This includes everything put into AdvertiseData including UUIDs, device info, &
         *  arbitrary service or manufacturer data.
         *  Attempting to send packets over this limit will result in a failure with error code
         *  AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE. Catch this error in the
         *  onStartFailure() method of an AdvertiseCallback implementation.
         */

//        ParcelUuid uuid = new ParcelUuid(CharacteristicHelper.UUID_BLE_SHIELD_ADV);


//        AdvertiseData.Builder mBuilder = new AdvertiseData.Builder();
//        ByteBuffer mManufacturerData = ByteBuffer.allocate(24);
        //string.getBytes(StandardCharsets.UTF_8);
        //UUID s_uuid = UUID.fromString("78123456-0000-0000-0000-000000000000");


//        String uid = GattAttributes.BLE_SHIELD_ADV;
//        byte[] uuid = uid.getBytes();
//        Log.d(TAG, "uuid=" + uid);
//
//        //
//        StringBuilder sb = new StringBuilder();
//        for (byte b : uuid) {
//            sb.append(String.format("%02X ", b));
//        }
//        Log.d(TAG, "uuidHex=" + sb);

//        mManufacturerData.put(0, (byte) 0xBE); // Beacon Identifier
//        mManufacturerData.put(1, (byte) 0xAC); // Beacon Identifier
//        for(int i = 2; i <= 17; i++)
//        {
//            mManufacturerData.put(i, uuid[i - 2]); // adding the UUID
//        }
//        mManufacturerData.put(18, (byte) 0x00); // first byte of Major
//        mManufacturerData.put(19, (byte) 0x09); // second byte of Major
//        mManufacturerData.put(20, (byte) 0x00); // first minor
//        mManufacturerData.put(21, (byte) 0x06); // second minor
//        mManufacturerData.put(22, (byte) 0xB5); // txPower
//        mBuilder.addManufacturerData(224, uuid); // using google's company ID

        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        dataBuilder.setIncludeDeviceName(false);
        dataBuilder.setIncludeTxPowerLevel(false);
        dataBuilder.addServiceUuid(new ParcelUuid(CharacteristicHelper.UUID_BLE_SHIELD_ADV));


//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
//        Log.d(TAG, "startingAdvertising");
//        mBluetoothLeAdvertiser.startAdvertising(mAdvertiseSettings, mAdvertiseData, mAdvertiseCallback);

        /* For example - this will cause advertising to fail (exceeds size limit) */
        //String failureData = "asdghkajsghalkxcjhfa;sghtalksjcfhalskfjhasldkjfhdskf";
        //dataBuilder.addServiceData(Constants.Service_UUID, failureData.getBytes());

        return dataBuilder.build();
    }

    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private AdvertiseSettings buildAdvertiseSettings() {
        return new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setConnectable(false)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();
    }

    /**
     * Custom callback after Advertising succeeds or fails to start. Broadcasts the error code
     * in an Intent to be picked up by AdvertiserFragment and stops this Service.
     */
    private class SampleAdvertiseCallback extends AdvertiseCallback {

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);

            Log.e(TAG, "Advertising failed....");
            Log.e("error code","" + errorCode);
            sendFailureIntent(errorCode);
            stopSelf();

        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.e(TAG, "Advertising successfully started.....");
        }
    }

    /**
     * Builds and sends a broadcast intent indicating Advertising has failed. Includes the error
     * code as an extra. This is intended to be picked up by the {@code AdvertiserFragment}.
     */
    private void sendFailureIntent(int errorCode) {
        Intent failureIntent = new Intent();
        failureIntent.setAction(ADVERTISING_FAILED);
        failureIntent.putExtra(ADVERTISING_FAILED_EXTRA_CODE, errorCode);
        sendBroadcast(failureIntent);
    }


    public class LocalBinder extends Binder {
        public AdvertiserService getService() {
            return AdvertiserService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new IRemoteAdvertiseService.Stub() {

            @Override
            public void onInit() throws RemoteException {
                if (initialize()) {
                    startAdvertising();
                } else {
                    Log.e(TAG, "Unable to initialize Bluetooth");
                    // TODO: maybe restart scanning and start all over again.. show a retry screen
                    if (getSystemService(Context.BLUETOOTH_SERVICE) == null) {
                        broadcastUpdate(ACTION_DEVICE_WITHOUT_BLUETOOTH);
                    } else {
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                            broadcastUpdate(ACTION_REQUEST_BLUETOOTH_PERMISSION);
                        }
                    }
                }
            }

            @Override
            public void stopAdvertising() throws RemoteException {
                stopAdvertising_();
            }
        };
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind","here");
        running = false;
        stopAdvertising_();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("onCreate","here");
    }
}
