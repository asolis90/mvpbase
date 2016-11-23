package com.asolis.mvpexample.ble.util;

/**
 * Created by angelsolis on 11/19/16.
 */

public class CharacteristicHelper {

    // client - sending data to ble device
    public static String BLE_DEVICE_LIGHT_ON = "led_on";
    public static String BLE_DEVICE_LIGHT_OFF = "led_off";
    public static String BLE_DEVICE_STATUS = "status";
    public static String BLE_DEVICE_ENROLL_COUNT = "enroll_count";
    public static String BLE_DEVICE_ENROLL_START = "enroll_start";
    public static String BLE_DEVICE_FINGERPRINT_DELETE_ALL = "delete_all";

    // client receiving data from ble device
    public static final String FINGERPRINT_SETUP_ENROLL_REMOVE_FINGER = "72656d6f7665";
    public static final String FINGERPRINT_SETUP_ENROLL_PRESS_FINGER = "7072657373";
    public static final String FINGERPRINT_SETUP_ENROLL_PRESS_SAME_FINGER = "70726573732a";
    public static final String FINGERPRINT_SETUP_ENROLL_SUCCESS = "713d656e72726f6cx6c73756363657373";
    public static final String FINGERPRINT_SETUP_ENROLL__ERROR = "713d656e72726f6c6c657272";
    public static final String FINGERPRINT_DELETE_ALL_SUCCESS = "713d64656c657465616c6c2d73756363";
    public static final String FINGERPRINT_DELETE_ALL_ERROR = "713d64656c657465616c6c2d657272";
    // need to compare this and then get the last hex of the value data received
    public static final String FINGERPRINT_ENROLL_COUNT = "713d656e726f6c6c636f756e742d";
}
