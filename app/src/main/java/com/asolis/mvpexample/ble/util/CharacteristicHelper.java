package com.asolis.mvpexample.ble.util;

import com.asolis.mvpexample.ble.attributes.GattAttributes;

import java.util.UUID;

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
    public static String BLE_DEVICE_ENROLL_1 = "enroll1";
    public static String BLE_DEVICE_ENROLL_2 = "enroll2";
    public static String BLE_DEVICE_ENROLL_3 = "enroll3";
    public static String BLE_DEVICE_FINGERPRINT_DELETE_ALL = "delete_all";
    public static String BLE_DEVICE_FINGERPRINT_DELETE = "delete-";
    public static String BLE_DEVICE_DISCONNECT = "disconnect";
    public static String BLE_DEVICE_FINGERPRINT_ENROLL_CANCEL = "enroll_cancel";

    // client receiving data from ble device
    public static final String FINGERPRINT_SETUP_ENROLL_START = "713d656e72726f6c6c73746172740000";
    public static final String FINGERPRINT_SETUP_ENROLL_1_REMOVE_FINGER = "72656d6f766531000000000000000000";
    public static final String FINGERPRINT_SETUP_ENROLL_2_REMOVE_FINGER = "72656d6f766532000000000000000000";
    public static final String FINGERPRINT_SETUP_ENROLL_3_REMOVE_FINGER = "72656d6f766533000000000000000000";
    public static final String FINGERPRINT_SETUP_ENROLL_PRESS_FINGER = "70726573730000000000000000000000";
    public static final String FINGERPRINT_SETUP_ENROLL_PRESS_SAME_FINGER = "70726573732a00000000000000000000";
    public static final String FINGERPRINT_SETUP_ENROLL_SUCCESS = "713d656e72726f6c6c73756363657373";
    public static final String FINGERPRINT_SETUP_ENROLL_ERROR = "713d656e72726f6c6c65727200000000";

    public static final String FINGERPRINT_DELETE_ALL_SUCCESS = "713d64656c657465616c6c7375636300";
    public static final String FINGERPRINT_DELETE_ALL_ERROR = "713d64656c657465616c6c6572720000";
    public static final String FINGERPRINT_DELETE_SUCCESS = "713d64656c6574657375636365737300";
    public static final String FINGERPRINT_DELETE_ERROR = "713d64656c6574656572720000000000";

    // need to compare this and then get the last hex of the value data received
//    public static final String FINGERPRINT_ENROLL_COUNT = "713d656e726f6c6c636f756e742d";
    public static final String FINGERPRINT_ENROLL_COUNT = "713d696473636f756e742d";
    public static final String FINGERPRINT_ENROLL_CANCEL= "713d656e726f6c6c63616e63656c0000";

    public final static UUID UUID_BLE_SHIELD_TX = UUID
            .fromString(GattAttributes.BLE_SHIELD_TX);
    public final static UUID UUID_BLE_SHIELD_NOTIFY = UUID
            .fromString(GattAttributes.BLE_SHIELD_NOTIFY);
    public final static UUID UUID_BLE_SHIELD_SERVICE = UUID
            .fromString(GattAttributes.BLE_SHIELD_SERVICE);
    public final static UUID UUID_BLE_SHIELD_ADV = UUID
            .fromString(GattAttributes.BLE_SHIELD_ADV);
}
