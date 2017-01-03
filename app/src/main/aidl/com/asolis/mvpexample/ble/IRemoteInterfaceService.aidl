// IRemoteInterfaceService.aidl
package com.asolis.mvpexample.ble;

import com.asolis.mvpexample.ble.IRemoteInterfaceCallback;

// Declare any non-default types here with import statements

interface IRemoteInterfaceService {
    boolean startBLEService();
    void onInit();
    void registerCallback(IRemoteInterfaceCallback callback);
    void getFingerPrintCount();
    void startFingerprintEnroll();
    void startFingerprintEnroll_1();
    void startFingerprintEnroll_2();
    void startFingerprintEnroll_3();
    void deleteFingerPrint(int id);
    void deleteAllFingerprints();
    boolean isDeviceConnected();
    boolean reconnectToDevice();
    void disconnect();
    void startAdvertiseService();
    void cancelEnroll();
}
