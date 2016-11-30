package com.asolis.mvpexample.ui.main;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.ble.util.CharacteristicHelper;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.recyclerview.adapter.DrawerAdapter;
import com.asolis.mvpexample.recyclerview.models.DrawerItem;
import com.asolis.mvpexample.ui.base.ble.BaseBLEActivity;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollFragment;
import com.asolis.mvpexample.util.PreferenceManager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseBLEActivity<MainActivityPresenter, MainActivityView> implements MainActivityView {

    @Inject
    MainActivityPresenter mPresenter;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
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
        ButterKnife.bind(this);
        prepareDrawer();
        getPresenter().onInit();
    }

    @Override
    public void onDataAvailable(String data) {
        switch (data) {
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_START:
                Log.e("fingerprint setup", "start");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, FingerprintEnrollFragment.newInstance(),
                                FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG)
                        .addToBackStack(null)
                        .commit();
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_FINGER:
                Log.e("fingerprint setup", "press finger");
                FingerprintEnrollFragment fef_press = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
                if (fef_press != null) {
                    fef_press.doUpdateUI(fef_press.UPDATE_TYPE_PRESS_FINGER);
                }
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_REMOVE_FINGER:
                Log.e("fingerprint setup", "remove finger");
                FingerprintEnrollFragment fef_remove = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
                if (fef_remove != null) {
                    fef_remove.doUpdateUI(fef_remove.UPDATE_TYPE_REMOVE_FINGER);
                }
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_SAME_FINGER:
                Log.e("fingerprint setup", "press again finger");
                FingerprintEnrollFragment fef_press_again = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
                if (fef_press_again != null) {
                    fef_press_again.doUpdateUI(fef_press_again.UPDATE_TYPE_PRESS_FINGER_AGAIN);
                }
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_SUCCESS:
                Log.e("fingerprint setup", "success");
                FingerprintEnrollFragment fef_success = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
                if (fef_success != null) {
                    fef_success.doUpdateUI(fef_success.UPDATE_TYPE_SUCCESS);
                }
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_ERROR:
                Log.e("fingerprint setup", "error");
                FingerprintEnrollFragment fef_error = (FingerprintEnrollFragment) getSupportFragmentManager().findFragmentByTag(FingerprintEnrollFragment.FRAGMENT_FINGERPRINT_TAG);
                if (fef_error != null) {
                    fef_error.doUpdateUI(fef_error.UPDATE_TYPE_ERROR);
                }
                break;
            case CharacteristicHelper.FINGERPRINT_DELETE_ALL_SUCCESS:
                Toast.makeText(getApplicationContext(), "Successfully deleted all Fingerprints!", Toast.LENGTH_SHORT).show();
                FingerprintFragment ff_delete_all = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                if (ff_delete_all != null) {
                    // clear all the fingerprints
                    ff_delete_all.clearData();
                }
                break;
            case CharacteristicHelper.FINGERPRINT_DELETE_ALL_ERROR:
                Toast.makeText(getApplicationContext(), "There was an error deleting all the Fingerprints", Toast.LENGTH_SHORT).show();
                break;
            default:
                if (data.startsWith(CharacteristicHelper.FINGERPRINT_ENROLL_COUNT)) {
                    String count = data.substring(data.lastIndexOf(CharacteristicHelper.FINGERPRINT_ENROLL_COUNT) + 1);
                    // need to check if the string data has already the integer
                    Log.e("count", "" + count);
                    FingerprintFragment ff_default = (FingerprintFragment) getSupportFragmentManager().findFragmentByTag(FingerprintFragment.FRAGMENT_FINGERPRINT_TAG);
                    if (ff_default != null && ff_default.isVisible()) {
                        ff_default.prepareData(count);
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
        mDrawerItemList.add(new DrawerItem("Home", R.drawable.ic_launcher));
        mDrawerItemList.add(new DrawerItem("Lights", R.drawable.ic_launcher));
        mDrawerItemList.add(new DrawerItem("Fingerprints", R.drawable.ic_launcher));
        DrawerAdapter mDrawerAdapter = new DrawerAdapter(getApplicationContext(), mDrawerItemList);
        mRecyclerView.setAdapter(mDrawerAdapter);
        mDrawerAdapter.setOnclickListener(new DrawerAdapter.OnclickListener() {
            @Override
            public void onClick(View view, DrawerItem item) {
                getPresenter().onDrawerClicked(item);
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

    // get fingerprint count
    public void getFingerprintCount() {
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_ENROLL_COUNT;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tmp);
        mBluetoothLeService.writeCharacteristic(characteristic);
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
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_FINGERPRINT_DELETE_ALL;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tmp);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

    // start enroll of fingerprints
    public void startFingerprintEnroll() {
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_ENROLL_START;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

    // start enroll_1 of fingerprint
    public void startFingerprintEnroll_1() {
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_ENROLL_1;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

    // start enroll_2 of fingerprint
    public void startFingerprintEnroll_2() {
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_ENROLL_2;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }

    // start enroll_3 of fingerprint
    public void startFingerprintEnroll_3() {
        BluetoothGattCharacteristic characteristic = mBluetoothLeService
                .getCharacteristic(BLEService.UUID_BLE_SHIELD_TX);

        String str = CharacteristicHelper.BLE_DEVICE_ENROLL_3;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[16];
        for (int i = 0; i < tmp.length; i++) {
            tx[i] = tmp[i];
        }
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);
    }
}
