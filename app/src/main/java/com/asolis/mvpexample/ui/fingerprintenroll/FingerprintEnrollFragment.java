package com.asolis.mvpexample.ui.fingerprintenroll;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseFragment;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.hookedonplay.decoviewlib.DecoView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FingerprintEnrollFragment extends BaseFragment<FingerprintEnrollFragmentPresenter, FingerprintEnrollFragmentView> implements FingerprintEnrollFragmentView {

    @Inject
    FingerprintEnrollFragmentPresenter mPresenter;

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

    public static String FRAGMENT_FINGERPRINT_TAG = "fingerprint-enroll";

    public static FingerprintEnrollFragment newInstance() {
        return new FingerprintEnrollFragment();
    }

    @Override
    public FingerprintEnrollFragmentPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onInit();
        ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_enroll, container, false);
        return view;
    }

    @Override
    public void injectSelf(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public void doShowToast() {
    }

    @Override
    public void doShowError(String s) {
    }

    @Override
    public void doShowNetworkError() {
    }

    @Override
    public void doEnroll_1() {
        Log.e("doEnroll_1","- here");
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startFingerprintEnroll_1();

    }

    @Override
    public void doEnroll_2() {
        Log.e("doEnroll_2"," here");
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startFingerprintEnroll_2();
    }

    @Override
    public void doEnroll_3() {
        Log.e("doEnroll_3"," here");
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startFingerprintEnroll_3();
    }

    @Override
    public void doUpdateUI(String type) {
        switch (type) {
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
                Toast.makeText(getContext(), "Added Fingerprint Successfully!", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case UPDATE_TYPE_ERROR:
                Toast.makeText(getContext(), "An error ocurred while enrolling fingerprint.", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStackImmediate(null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
