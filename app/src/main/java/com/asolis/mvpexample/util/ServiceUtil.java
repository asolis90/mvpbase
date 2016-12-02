package com.asolis.mvpexample.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by angelsolis on 12/9/16.
 */

public class ServiceUtil {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
