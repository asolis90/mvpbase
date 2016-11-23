package com.asolis.mvpexample.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.asolis.mvpexample.ble.attributes.GattAttributes;

import java.util.List;
import java.util.UUID;

/**
 * Created by angelsolis on 11/19/16.
 */

public class BLEService extends Service {
    private static final String TAG = "BLEService";
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt mGatt;
    String mBluetoothDeviceAddress;

    public final static UUID UUID_BLE_SHIELD_TX = UUID
            .fromString(GattAttributes.BLE_SHIELD_TX);
    public final static UUID UUID_BLE_SHIELD_NOTIFY = UUID
            .fromString(GattAttributes.BLE_SHIELD_NOTIFY);
    public final static UUID UUID_BLE_SHIELD_SERVICE = UUID
            .fromString(GattAttributes.BLE_SHIELD_SERVICE);

    public final static String ACTION_GATT_CONNECTED =
            "com.asolis.mvpexample.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_STOP_SCANNING =
            "com.asolis.mvpexample.ACTION_GATT_STOP_SCANNING";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.asolis.mvpexample.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.asolis.mvpexample.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.asolis.mvpexample.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DEVICE_NOT_FOUND =
            "com.asolis.mvpexample.ACTION_DEVICE_NOT_FOUND";
    public final static String ACTION_DEVICE_NULL_ERROR =
            "com.asolis.mvpexample.ACTION_DEVICE_NULL_ERROR";

    public final static String EXTRA_DATA =
            "com.asolis.mvpexample.EXTRA_DATA";

    public class LocalBinder extends Binder {
        public BLEService getService() {
            return BLEService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }


    public boolean connect(final String address) {
        if (address == null) {
            Log.w(TAG, "unspecified address..");
            broadcastUpdate(ACTION_DEVICE_NULL_ERROR);
            return false;
        }

        // Previously connected device. Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mGatt != null) {
            if (mGatt.connect()) {
                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        // TODO: maybe check list of devices connected and if the address matches then try to connect
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            broadcastUpdate(ACTION_DEVICE_NOT_FOUND);
            return false;
        }
        mGatt = device.connectGatt(this, false, gattCallback);

        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mGatt.disconnect();
    }


    /**
     * Gatt Callback
     */
    public BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e("gattCallback", "STATE_CONNECTED");
                    broadcastUpdate(ACTION_GATT_STOP_SCANNING);
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    broadcastUpdate(ACTION_GATT_DISCONNECTED);
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.e("status", "" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mGatt = gatt;
                List<BluetoothGattService> services = mGatt.getServices();
                Log.e("onServicesDiscovered", services.toString());
                Log.e("services", "" + services.size());
                if (services.size() != 0) {
                    BluetoothGattService bleService = gatt.getService(UUID_BLE_SHIELD_SERVICE);
                    if (bleService != null) {
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    }
                }
            } else {
                Log.e("onServicesDiscovered", "status: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
//            gatt.disconnect();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        if (UUID_BLE_SHIELD_NOTIFY.equals(characteristic.getUuid())) {
            final byte[] data = characteristic.getValue();
            intent.putExtra(EXTRA_DATA, data);
        }
        sendBroadcast(intent);
    }

    /**
     * Retrieves supported GATT service on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public BluetoothGattService getSupportedGattService() {
        if (mGatt == null)
            return null;

        return mGatt.getService(UUID_BLE_SHIELD_SERVICE);
    }

    public BluetoothGattCharacteristic getCharacteristic(UUID uuid) {
        if (mGatt == null)
            return null;
        return mGatt.getService(UUID_BLE_SHIELD_SERVICE).getCharacteristic(uuid);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.e(TAG, "BluetoothAdapter or Gatt not initialized");
            return;
        }
        mGatt.writeCharacteristic(characteristic);
    }
}
