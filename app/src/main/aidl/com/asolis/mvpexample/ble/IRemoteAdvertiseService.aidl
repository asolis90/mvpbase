// IRemoteAdvertiseService.aidl
package com.asolis.mvpexample.ble;

// Declare any non-default types here with import statements

interface IRemoteAdvertiseService {
    void onInit();
    void stopAdvertising();
}
