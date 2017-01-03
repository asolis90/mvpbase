package com.asolis.mvpexample.ui.home;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ble.BLEService;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseFragment;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.util.PreferenceManager;
import com.asolis.mvpexample.util.ServiceUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter, HomeFragmentView> implements HomeFragmentView {

    @Inject
    HomeFragmentPresenter mPresenter;
    @Bind(R.id.fragment_home_status_iv)
    ImageView mStatusImageView;
    @Bind(R.id.fragment_home_status_tv)
    TextView mStatusTextView;
    @Bind(R.id.fragment_home_connect_btn)
    Button mReconnectBtn;

    public static String FRAGMENT_HOME_TAG = "home";

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public HomeFragmentPresenter getPresenter() {
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (ServiceUtil.isMyServiceRunning(getContext(), BLEService.class)) {
            if (mainActivity.mRemoteService != null) {
                try {
                    if (mainActivity.mRemoteService.isDeviceConnected()) {
                        inflater.inflate(R.menu.main, menu);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (item.getItemId()) {
            case R.id.action_disconnect:
                // disconnect
                Log.e("do disconnect","here");
                mainActivity.doDisconnect();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("onViewCreated", "here");
        ButterKnife.bind(this, view);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (ServiceUtil.isMyServiceRunning(getContext(), BLEService.class)) {
            try {
                if (mainActivity.mRemoteService != null) {
                    Log.e("status conneceted", "here");
                    if (mainActivity.mRemoteService.isDeviceConnected()) {
                        setStatusConnected();
                    } else {
                        setStatusDisconnected();
                    }
                } else {
                    Log.e("status disconneceted", "here");
                    setStatusDisconnected();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            setStatusDisconnected();
        }
    }

    public void setStatusConnected() {
        mStatusTextView.setText("Connected");
        mStatusImageView.setImageResource(R.drawable.status_icon);
        mReconnectBtn.setVisibility(View.GONE);
    }

    public void setStatusDisconnected() {
        mStatusTextView.setText("Disconnected");
        mStatusImageView.setImageResource(R.drawable.status_icon_off);
        mReconnectBtn.setVisibility(View.VISIBLE);
    }

    public void refreshMenu() {
        getActivity().invalidateOptionsMenu();
    }

    @OnClick(R.id.fragment_home_connect_btn)
    public void onClick(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startAdvertisingService();
    }
}
