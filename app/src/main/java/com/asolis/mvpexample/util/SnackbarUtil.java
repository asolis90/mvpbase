package com.asolis.mvpexample.util;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

public class SnackbarUtil {
    private static String TAG = SnackbarUtil.class.getSimpleName();

    public static void show(@Nullable View view, int resID) {
        if (view != null) {
            Snackbar.make(view, resID, Snackbar.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "Snackbar failed to display");
        }
    }

    public static void show(@Nullable View view, String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        } else {
            Log.e(TAG, "Snackbar failed to display");
        }
    }

    public static void showWithAction(@Nullable View view, String msg, String actionText,
                                      View.OnClickListener onClickListener) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(actionText, onClickListener).show();
        } else {
            Log.e(TAG, "Snackbar failed to display");
        }
    }
}
