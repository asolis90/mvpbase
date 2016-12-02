package com.asolis.mvpexample.ui.fingerprintenroll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.util.CharacteristicHelper;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.ble.BaseBLEActivity;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.hookedonplay.decoviewlib.DecoView;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by angelsolis on 12/27/16.
 */

public class FingerprintEnrollActivity
        extends BaseBLEActivity<FingerprintEnrollActivityPresenter, FingerprintEnrollActivityView>
        implements FingerprintEnrollActivityView
{

    @Inject
    FingerprintEnrollActivityPresenter mPresenter;

    @Bind(R.id.fragment_fingerprint_enroll_title)
    TextView mTitle;

    @Bind(R.id.fragment_fingerprint_enroll_title_sub)
    TextView mSubTitle;

    @Bind(R.id.fragment_fingerprint_enroll_dv)
    DecoView mDecoView;

    public final String UPDATE_TYPE_PRESS_FINGER = "press-finger";
    public final String UPDATE_TYPE_PRESS_FINGER_AGAIN = "press-finger-again";
    public final String UPDATE_TYPE_REMOVE_FINGER_1 = "remove-finger-1";
    public final String UPDATE_TYPE_REMOVE_FINGER_2 = "remove-finger-2";
    public final String UPDATE_TYPE_REMOVE_FINGER_3 = "remove-finger-3";
    public final String UPDATE_TYPE_SUCCESS = "success";
    public final String UPDATE_TYPE_ERROR = "error";
    public final String UPDATE_TYPE_CANCEL = "cancel";

    public static String FINGERPRINT_ENROLL_TAG = "fingerprint-enroll";

    private static int REQUEST_CODE = 2;

    public static void launch(Activity callingActivity)
    {
        Intent intent = new Intent(callingActivity, FingerprintEnrollActivity.class);
        callingActivity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fingerprint_enroll);
        getPresenter().onInit();
        ButterKnife.bind(this);
    }

    @Override
    public void doShowToast()
    {

    }

    @Override
    public void doSetToolbarTitle(String title)
    {

    }

    @Override
    public void doEnroll_1()
    {
        Log.e("doEnroll_1", "- here");
        startFingerprintEnroll_1();
    }

    @Override
    public void doEnroll_2()
    {
        Log.e("doEnroll_2", " here");
        startFingerprintEnroll_2();
    }

    @Override
    public void doEnroll_3()
    {
        Log.e("doEnroll_3", " here");
        startFingerprintEnroll_3();
    }

    @Override
    public void doUpdateUI(String type)
    {
        switch(type)
        {
            case UPDATE_TYPE_PRESS_FINGER:
                // TODO: set the decoView to be filled to a certain percentage...
//                doEnroll_2();
                mTitle.setText(getString(R.string.press_same_finger_again));
                mSubTitle.setText(getString(R.string.press_same_finger_again));
                break;
            case UPDATE_TYPE_PRESS_FINGER_AGAIN:
                mTitle.setText(getString(R.string.start_enrollment_3));
                mSubTitle.setText(getString(R.string.press_same_finger_again));
                break;
            case UPDATE_TYPE_REMOVE_FINGER_1:
                mTitle.setText(getString(R.string.remove_finger));
                mSubTitle.setText(getString(R.string.remove_finger_sub));
                doEnroll_2();
                break;
            case UPDATE_TYPE_REMOVE_FINGER_2:
                mTitle.setText(getString(R.string.remove_finger));
                mSubTitle.setText(getString(R.string.remove_finger_sub));
                doEnroll_3();
                break;
            case UPDATE_TYPE_REMOVE_FINGER_3:
                mTitle.setText(getString(R.string.remove_finger));
                mSubTitle.setText(getString(R.string.remove_finger_sub));
                break;
            case UPDATE_TYPE_SUCCESS:
                mTitle.setText(getString(R.string.fingerprint_added));
                mSubTitle.setText(getString(R.string.fingerprint_added_sub));
                Toast.makeText(getApplicationContext(), "Added Fingerprint Successfully!", Toast
                        .LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("response", true);
                Log.e("setting result", "here");
                setResult(RESULT_OK, intent);
                finish();
                break;
            case UPDATE_TYPE_ERROR:
                Toast.makeText(getApplicationContext(), "An error ocurred while enrolling " +
                        "fingerprint.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case UPDATE_TYPE_CANCEL:
                Toast.makeText(getApplicationContext(), "Fingerprint Timeout.", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public FingerprintEnrollActivityPresenter getPresenter()
    {
        return mPresenter;
    }

    @Override
    public void injectSelf(ApplicationComponent component)
    {
        component.inject(this);
    }


    @Override
    public void prepareToolbar(ActionBar actionBar)
    {

    }


    @Override
    public void onDataAvailable(byte[] data)
    {
        String result = new BigInteger(1, data).toString(16);
        Log.e(FINGERPRINT_ENROLL_TAG, "onDataAvailable - result hex: " + result);
        switch(result)
        {
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_FINGER:
                doUpdateUI(UPDATE_TYPE_PRESS_FINGER);
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_1_REMOVE_FINGER:
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_2_REMOVE_FINGER:
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_3_REMOVE_FINGER:
                switch(result)
                {
                    case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_1_REMOVE_FINGER:
                        Log.e("UPDATETYPEREMOVEFINGER1", "here");
                        doUpdateUI(UPDATE_TYPE_REMOVE_FINGER_1);
                        break;
                    case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_2_REMOVE_FINGER:
                        doUpdateUI(UPDATE_TYPE_REMOVE_FINGER_2);
                        break;
                    case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_3_REMOVE_FINGER:
                        doUpdateUI(UPDATE_TYPE_REMOVE_FINGER_3);
                        break;
                }
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_PRESS_SAME_FINGER:
                Log.e("fingerprint setup", "press again finger");
                doUpdateUI(UPDATE_TYPE_PRESS_FINGER_AGAIN);

                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_SUCCESS:
                Log.e("fingerprint setup", "success");
                doUpdateUI(UPDATE_TYPE_SUCCESS);
                break;
            case CharacteristicHelper.FINGERPRINT_SETUP_ENROLL_ERROR:
                Log.e("fingerprint setup", "error");
                doUpdateUI(UPDATE_TYPE_ERROR);
                break;
            case CharacteristicHelper.FINGERPRINT_ENROLL_CANCEL:
                doUpdateUI(UPDATE_TYPE_CANCEL);
                break;
        }
    }

    @Override
    public void onDeviceNotFound()
    {

    }

    // start enroll_1 of fingerprint
    public void startFingerprintEnroll_1()
    {
        try
        {
            if(mRemoteService != null)
            {
                mRemoteService.startFingerprintEnroll_1();
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
    }

    // start enroll_2 of fingerprint
    public void startFingerprintEnroll_2()
    {
        try
        {
            if(mRemoteService != null)
            {
                mRemoteService.startFingerprintEnroll_2();
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
    }

    // start enroll_3 of fingerprint
    public void startFingerprintEnroll_3()
    {
        try
        {
            if(mRemoteService != null)
            {
                mRemoteService.startFingerprintEnroll_3();
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case android.R.id.home:
                try
                {
                    if(mRemoteService != null)
                    {
                        mRemoteService.cancelEnroll();
                    }
                }
                catch(RemoteException e)
                {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        try
        {
            if(mRemoteService != null)
            {
                mRemoteService.cancelEnroll();
            }
        }
        catch(RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
