package com.asolis.mvpexample.util;

import android.content.Context;

/**
 * Created by angelsolis on 11/19/16.
 */

public class PreferenceManager {

    private static final String PREFERENCE_FILE = "tech-bag-app-preference";
    private static final String PREF_DEVICE_STATUS = "device-status";
    private static final String PREF_DEVICE_ADDRESS = "device-address";

    // Device Status

    public static void setDeviceStatus(Context context, boolean value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit()
                .putBoolean(PREF_DEVICE_STATUS, value).apply();
    }

    public static boolean getDeviceStatus(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE,
                Context.MODE_PRIVATE).getBoolean(PREF_DEVICE_STATUS, false);
    }

    // Device Address

    public static void setDeviceAddress(Context context, String value) {
        context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE).edit()
                .putString(PREF_DEVICE_ADDRESS, value).apply();
    }

    public static String getDeviceAddress(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE,
                Context.MODE_PRIVATE).getString(PREF_DEVICE_ADDRESS, null);
    }
}
