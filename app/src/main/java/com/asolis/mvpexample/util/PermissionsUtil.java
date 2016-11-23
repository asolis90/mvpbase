package com.asolis.mvpexample.util;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by angelsolis on 9/6/16.
 */

public class PermissionsUtil {

    public static void checkWriteStoragePermission(final Activity activity,
                                                   final String errorMessage,
                                                   final PermissionsUtil.PermissionUtilCallback callback) {
        if (Nammu.hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            callback.onResult(true);
        } else {

            Nammu.askForPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    callback.onResult(true);
                }

                @Override
                public void permissionRefused() {
                    Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                    callback.onResult(false);
                }
            });
        }
    }

    public static void checkLocationPermission(final Activity activity,
                                               final String errorMessage,
                                               final PermissionsUtil.PermissionUtilCallback callback) {
        if (Nammu.hasPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                Nammu.hasPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            callback.onResult(true);
        } else {
            if (!Nammu.hasPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) &&
                    !Nammu.hasPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                String[] permissions = new String[2];
                permissions[0] = Manifest.permission.ACCESS_COARSE_LOCATION;
                permissions[1] = Manifest.permission.ACCESS_FINE_LOCATION;
                askForPermissions(activity, permissions, errorMessage, callback);
            } else if (!Nammu.hasPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                askForPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION, errorMessage, callback);
            } else {
                askForPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, errorMessage, callback);
            }
        }
    }

    private static void askForPermission(final Activity activity, String permission, final String errorMessage, final PermissionsUtil.PermissionUtilCallback callback) {
        Nammu.askForPermission(activity, permission, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                callback.onResult(true);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                callback.onResult(false);
            }
        });
    }

    private static void askForPermissions(final Activity activity, String[] permissions, final String errorMessage, final PermissionsUtil.PermissionUtilCallback callback) {
        Nammu.askForPermission(activity, permissions, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                callback.onResult(true);
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                callback.onResult(false);
            }
        });
    }

    public interface PermissionUtilCallback {
        void onResult(boolean result);
    }
}
