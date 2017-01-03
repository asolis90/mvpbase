package com.asolis.mvpexample.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.util.CharacteristicHelper;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.recyclerview.adapter.DrawerAdapter;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.recyclerview.models.FingerprintItem;
import com.asolis.mvpexample.ui.base.ble.BaseBLEActivity;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollActivity;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollFragment;
import com.asolis.mvpexample.util.PreferenceManager;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends BaseBLEActivity<MainActivityPresenter, MainActivityView> implements MainActivityView {

    @Inject
    MainActivityPresenter mPresenter;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.activity_main_drawer)
    RecyclerView mRecyclerView;

    public static void launch(Activity callingActivity) {
        Intent intent = new Intent(callingActivity, MainActivity.class);
        callingActivity.startActivity(intent);
    }

    @Override
    public MainActivityPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareDrawer();
        getPresenter().onInit();
    }

    @Override
    public void prepareToolbar(ActionBar actionBar) {

    }

    @Override
    public void onDataAvailable(byte[] data) {
        String result = new BigInteger(1, data).toString(16);
        Log.e("onDataAvailable" +
                "" +
                "" +
                "", "result hex: " + result);
        switch (result) {
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_START:
                Log.e("fingerprint setup", "start");
                FingerprintEnrollActivity.launch(this);
                break;
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_FINGER:
//                Log.e("fingerprint setup", "press finger");
//                FingerprintEnrollFragment fef_press = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
//                if (fef_press != null) {
//                    fef_press.doUpdateUI(fef_press.UPDATE_TYPE_PRESS_FINGER);
//                }
//                break;
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_1_REMOVE_FINGER:
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_2_REMOVE_FINGER:
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_3_REMOVE_FINGER:
//                FingerprintEnrollFragment fef_remove = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
//                if (fef_remove != null) {
//                    switch (result) {
//                        case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_1_REMOVE_FINGER:
//                            Log.e("UPDATETYPEREMOVEFINGER1", "here");
//                            fef_remove.doUpdateUI(fef_remove.UPDATE_TYPE_REMOVE_FINGER_1);
//                            break;
//                        case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_2_REMOVE_FINGER:
//                            fef_remove.doUpdateUI(fef_remove.UPDATE_TYPE_REMOVE_FINGER_2);
//                            break;
//                        case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_3_REMOVE_FINGER:
//                            fef_remove.doUpdateUI(fef_remove.UPDATE_TYPE_REMOVE_FINGER_3);
//                            break;
//                    }
//                }
//                break;
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_SAME_FINGER:
//                Log.e("fingerprint setup", "press again finger");
//                FingerprintEnrollFragment fef_press_again = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
//                if (fef_press_again != null) {
//                    fef_press_again.doUpdateUI(fef_press_again.UPDATE_TYPE_PRESS_FINGER_AGAIN);
//                }
//                break;
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_SUCCESS:
//                Log.e("fingerprint setup", "success");
//                FingerprintEnrollFragment fef_success = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
//                if (fef_success != null) {
//                    fef_success.doUpdateUI(fef_success.UPDATE_TYPE_SUCCESS);
//                }
//                break;
//            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_ERROR:
//                Log.e("fingerprint setup", "error");
//                FingerprintEnrollFragment fef_error = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
//                if (fef_error != null) {
//                    fef_error.doUpdateUI(fef_error.UPDATE_TYPE_ERROR);
//                }
//                break;
            case CharacteristicHelper.FINGERPRINT_DELETE_ALL_SUCCESS:
                Toast.makeText(getApplicationContext(), "Successfully deleted all Fingerprints!", Toast.LENGTH_SHORT).show();
                FingerprintFragment ff_delete_all = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                if (ff_delete_all != null) {
                    // clear all the fingerprints
                    ff_delete_all.clearData();
                }
                break;
            case CharacteristicHelper.FINGERPRINT_DELETE_ALL_ERROR:
                Toast.makeText(getApplicationContext(), "Unable to delete all fingerprints", Toast.LENGTH_SHORT).show();
                break;

            case CharacteristicHelper.FINGERPRINT_DELETE_SUCCESS:
                FingerprintFragment ff_delete = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                if (ff_delete != null) {
                    // clear all the fingerprints
                    ff_delete.deleteSuccessful();
                }
                break;
            case CharacteristicHelper.FINGERPRINT_DELETE_ERROR:
                Toast.makeText(getApplicationContext(), "Unable to delete fingerprint", Toast.LENGTH_SHORT).show();
                break;
            default:
                if (result.startsWith(CharacteristicHelper.FINGERPRINT_ENROLL_COUNT)) {
                    String count = result.substring(result.lastIndexOf("2d") + 2);
                    String c = count.replaceAll("0", "");
                    if (c.isEmpty() || c.equals("")) {
                        c = "0";
                    }

                    char[] digits1 = c.toCharArray();
                    ArrayList<Integer> ids = new ArrayList();
                    for (int i = 0; i < digits1.length; i++) {
                        Log.e("digits1[i]", "" + digits1[i]);
                        if (digits1[i] != '0') {
                            Log.e("char num", "" + Character.getNumericValue(digits1[i]));
                            ids.add(Character.getNumericValue(digits1[i]));
                        }
                    }
                    Log.e("ids - size", "" + ids.size());

                    // need to check if the string data has already the integer
                    FingerprintFragment ff_default = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                    if (ff_default != null) {
                        ff_default.prepareData(ids);
                    }
                }
                break;
        }
    }

    @Override
    public void onDeviceNotFound() {
        final Dialog dialog = new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.retry_dialog);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.WRAP_CONTENT);
        Button confirmBtn = (Button) dialog.findViewById(R.id.retry_dialog_retry);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mBluetoothLeService != null) {
                    if (mBluetoothLeService.initialize()) {
                        mBluetoothLeService.connect(PreferenceManager.getDeviceAddress(getApplicationContext()));
                    } else {
                        if (getSystemService(Context.BLUETOOTH_SERVICE) == null) {
                            Toast.makeText(getApplicationContext(), R.string.bluetooth_not_found, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            }
                        }
                    }
                }
            }
        });

        Button cancelBtn = (Button) dialog.findViewById(R.id.retry_dialog_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.unable_to_proceed, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        dialog.show();
    }

    /**
     * Prepares the drawer and hooks up the toggle
     */
    private void prepareDrawer() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        ArrayList<DrawerItem> mDrawerItemList = new ArrayList<>();
        mDrawerItemList.add(new DrawerItem());
        mDrawerItemList.add(new DrawerItem("Home", R.drawable.dashboard_icon));
        mDrawerItemList.add(new DrawerItem("Lights", R.drawable.lights_on_icon));
        mDrawerItemList.add(new DrawerItem("Fingerprints", R.drawable.fingerprint_icon));
        DrawerAdapter mDrawerAdapter = new DrawerAdapter(getApplicationContext(), mDrawerItemList);
        mRecyclerView.setAdapter(mDrawerAdapter);
        mDrawerAdapter.setOnclickListener(new DrawerAdapter.OnclickListener() {
            @Override
            public void onClick(View view, DrawerItem item) {
                getPresenter().onDrawerClicked(item);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mDrawerAdapter.setOnHeaderclickListener(new DrawerAdapter.OnHeaderClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void injectSelf(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void doShowToast() {
    }

    @Override
    public void doShowFragmentWithStack(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager
                .POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, fragmentTag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void doShowFragmentWithoutStack(Fragment fragment, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, fragmentTag)
                .commit();
    }

    @Override
    public void doSetToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    // get fingerprint count
    public void getFingerprintCount() {
        try {
            if (mRemoteService != null) {
                mRemoteService.getFingerPrintCount();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // delete all fingerprints
    public void deleteAllFingerprints() {
        try {
            if (mRemoteService != null) {
                mRemoteService.deleteAllFingerprints();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // start enroll of fingerprints
    public void startFingerprintEnroll() {
        try {
            if (mRemoteService != null) {
                mRemoteService.startFingerprintEnroll();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // start enroll_1 of fingerprint
    public void startFingerprintEnroll_1() {
        try {
            if (mRemoteService != null) {
                mRemoteService.startFingerprintEnroll_1();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // start enroll_2 of fingerprint
    public void startFingerprintEnroll_2() {
        try {
            if (mRemoteService != null) {
                mRemoteService.startFingerprintEnroll_2();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // start enroll_3 of fingerprint
    public void startFingerprintEnroll_3() {
        try {
            if (mRemoteService != null) {
                mRemoteService.startFingerprintEnroll_3();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteFingerPrint(FingerprintItem item) {
        try {
            if (mRemoteService != null) {
                mRemoteService.deleteFingerPrint(item.getEnrollId());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void doDisconnect() {
        try {
            if (mRemoteService != null) {
                Log.e("doDisconnect","disconnecting");
                mRemoteService.disconnect();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        PreferenceManager.setDisconnectManually(this, true);
    }
}
