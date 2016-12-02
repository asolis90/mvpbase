package com.asolis.mvpexample.ble;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.attributes.GattAttributes;
import com.asolis.mvpexample.ble.util.CharacteristicHelper;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.util.PreferenceManager;
import com.asolis.mvpexample.util.ServiceUtil;

import java.util.List;
import java.util.UUID;

/**
 * Created by angelsolis on 11/19/16.
 */

public class BLEService extends Service {
    private static final String TAG = "BLEService";
    private static boolean isConnectedToDevice = false;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothGatt mGatt;
    String mBluetoothDeviceAddress;
    IRemoteInterfaceCallback mCallback;

    public final static String ACTION_GATT_CONNECTED =
            "com.asolis.mvpexample.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_STOP_SCANNING =
            "com.asolis.mvpexample.ACTION_GATT_STOP_SCANNING";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.asolis.mvpexample.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_DEVICE_RETRY_NOT_FOUND =
            "com.asolis.mvpexample.ACTION_DEVICE_RETRY_NOT_FOUND";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.asolis.mvpexample.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_GATT_ALREADY_CONNECTED =
            "com.asolis.mvpexample.ACTION_GATT_ALREADY_CONNECTED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.asolis.mvpexample.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DEVICE_NOT_FOUND =
            "com.asolis.mvpexample.ACTION_DEVICE_NOT_FOUND";
    public final static String ACTION_DEVICE_NULL_ERROR =
            "com.asolis.mvpexample.ACTION_DEVICE_NULL_ERROR";
    public final static String ACTION_DEVICE_WITHOUT_BLUETOOTH =
            "com.asolis.mvpexample.ACTION_DEVICE_WITHOUT_BLUETOOTH";
    public final static String ACTION_REQUEST_BLUETOOTH_PERMISSION =
            "com.asolis.mvpexample.ACTION_REQUEST_BLUETOOTH_PERMISSION";

    public final static String EXTRA_DATA =
            "com.asolis.mvpexample.EXTRA_DATA";

    @Override
    public IBinder onBind(Intent intent) {
        return new IRemoteInterfaceService.Stub() {

            @Override
            public boolean startBLEService() throws RemoteException {
                return false;
            }

            @Override
            public void onInit() throws RemoteException {
                startConnection();
            }

            @Override
            public void registerCallback(IRemoteInterfaceCallback callback) throws RemoteException {
                mCallback = callback;
            }

            @Override
            public void getFingerPrintCount() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_ENROLL_COUNT);
            }

            @Override
            public void startFingerprintEnroll() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_ENROLL_START);
            }

            @Override
            public void startFingerprintEnroll_1() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_ENROLL_1);
            }

            @Override
            public void startFingerprintEnroll_2() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_ENROLL_2);
            }

            @Override
            public void startFingerprintEnroll_3() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_ENROLL_3);
            }

            @Override
            public void deleteFingerPrint(int id) throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_FINGERPRINT_DELETE + id);
            }

            @Override
            public void deleteAllFingerprints() throws RemoteException {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_FINGERPRINT_DELETE_ALL);
            }

            @Override
            public boolean isDeviceConnected() throws RemoteException {
                return isConnectedToDevice;
            }

            @Override
            public boolean reconnectToDevice() throws RemoteException {
                if (initialize()) {
                    String address = PreferenceManager.getDeviceAddress(getApplicationContext());
                    // Previously connected device. Try to reconnect.
                    if (mBluetoothAdapter != null) {
                        Log.e("mBluetoothAdapter", "not null");
                        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        mGatt = device.connectGatt(getApplicationContext(), true, gattCallback);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void disconnect() throws RemoteException {
                Log.e(TAG,"disconnect - here");
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_DISCONNECT);
            }

            @Override
            public void startAdvertiseService() throws RemoteException {
                startAdvertisingService();
            }

            @Override
            public void cancelEnroll() throws RemoteException
            {
                writeCharacteristic(CharacteristicHelper.BLE_DEVICE_FINGERPRINT_ENROLL_CANCEL);
            }
        };
    }

    private void startConnection() {
        Log.e("isConnectedToDevice", "" + isConnectedToDevice);
        if (!BLEService.isConnectedToDevice) {
            if (initialize()) {
                // Automatically connects to the device upon successful start-up
                // initialization.
                connect(PreferenceManager.getDeviceAddress(getApplicationContext()));
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
        } else {
            broadcastUpdate(ACTION_GATT_ALREADY_CONNECTED);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind", "here");
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
//        close();
        return super.onUnbind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy", "here");
        super.onDestroy();
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

//    private final IBinder mBinder = new LocalBinder();

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
        Log.e("connect", "here");
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


    private boolean isRunningonForeground = false;
    /**
     * Gatt Callback
     */

    public BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    if(!isConnectedToDevice) {
                        Log.e("gattCallback", "STATE_CONNECTED");
                        isConnectedToDevice = true;
                        if (!isRunningonForeground) {
                            runAsForeground();
                        }
                        broadcastUpdate(ACTION_GATT_STOP_SCANNING);
                        gatt.discoverServices();
//                    unbindService(mAdvertiseServiceConnection);
                        stopAdvertising();
                        isAdvertiseServiceBinded = false;
                    }
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    if (isConnectedToDevice) {
                        isConnectedToDevice = false;
                        stopForeground(true);
                        broadcastUpdate(ACTION_GATT_DISCONNECTED);
                        isRunningonForeground = false;
                        if (!PreferenceManager.isDisconnectedManually(getApplicationContext())) {
                            startAdvertisingService();
                            startConnection();
                        }
                    } else {
                        Log.e("gattCallback", "ACTION_DEVICE_RETRY_NOT_FOUND");
                        // this is a generic response from the gatt trying to connect to a device but failed
                        broadcastUpdate(ACTION_DEVICE_RETRY_NOT_FOUND);
                    }
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e("status", "success");
                mGatt = gatt;
                List<BluetoothGattService> services = mGatt.getServices();
                Log.e("services", "" + services.size());
                if (services.size() != 0) {
                    BluetoothGattService bleService = gatt.getService(CharacteristicHelper.UUID_BLE_SHIELD_SERVICE);
                    if (bleService != null) {
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                        enableCharacteristicNotification();
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
            Log.e("onCharacteristicChanged", "here");
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void stopAdvertising() {
        try {
            mAdvertiseService.stopAdvertising();
        } catch (RemoteException e) {
            Log.e("REMOTE EXCEPTION", "here - something happened");
            e.printStackTrace();
        }
    }

    /**
     * Enables or disables notification on a give characteristic.
     */
    public void enableCharacteristicNotification() {
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        BluetoothGattCharacteristic characteristic = getCharacteristic(CharacteristicHelper.UUID_BLE_SHIELD_NOTIFY);
        mGatt.setCharacteristicNotification(characteristic, true);
        BluetoothGattDescriptor descriptor = characteristic
                .getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mGatt.writeDescriptor(descriptor);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // TODO: Broadcast seems to not run correctly in a new fragment
    private void broadcastUpdate(final String action, BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        Log.e("broadcastUpdate", "" + characteristic.getUuid());
        if (CharacteristicHelper.UUID_BLE_SHIELD_NOTIFY.equals(characteristic.getUuid())) {
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

        return mGatt.getService(CharacteristicHelper.UUID_BLE_SHIELD_SERVICE);
    }

    public BluetoothGattCharacteristic getCharacteristic(UUID uuid) {
        if (mGatt == null)
            return null;
        return mGatt.getService(CharacteristicHelper.UUID_BLE_SHIELD_SERVICE).getCharacteristic(uuid);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.e(TAG, "BluetoothAdapter or Gatt not initialized");
            return;
        }
        mGatt.writeCharacteristic(characteristic);
    }

    public void writeCharacteristic(String data) {
        BluetoothGattCharacteristic characteristic = getCharacteristic(CharacteristicHelper.UUID_BLE_SHIELD_TX);
        String str = data;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tx);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || mGatt == null) {
            Log.e(TAG, "BluetoothAdapter or Gatt not initialized");
            return;
        }
        mGatt.writeCharacteristic(characteristic);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (isConnectedToDevice) {
            runAsForeground();
        }
    }

    private void runAsForeground() {
        isRunningonForeground = true;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bag_icon)
                .setContentText("Smart Bag connected!")
                .setContentIntent(pendingIntent).build();
        startForeground(123, notification);
    }

    public void startAdvertisingService() {
        Log.e("startAdvertisingService", "here");
        if(mAdvertiseService != null && ServiceUtil.isMyServiceRunning(getApplicationContext(), AdvertiserService.class)) {
            try
            {
                mAdvertiseService.onInit();
            }
            catch(RemoteException e)
            {
                e.printStackTrace();
            }

        } else {
            Intent advertiseIntent = new Intent(this, AdvertiserService.class);
            startService(advertiseIntent);
            bindService(advertiseIntent, mAdvertiseServiceConnection, 0); // bind AdvertiserService
        }
    }


    IRemoteAdvertiseService mAdvertiseService;
    boolean isAdvertiseServiceBinded = false;
    private final ServiceConnection mAdvertiseServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.e(TAG, "mAdvertiseServiceConnection - onServiceconnected here");
            mAdvertiseService = IRemoteAdvertiseService.Stub.asInterface((IBinder) service);
            try {
                mAdvertiseService.onInit();
                isAdvertiseServiceBinded = true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mAdvertiseService = null;
        }
    };

}
