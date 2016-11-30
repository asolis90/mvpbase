package com.asolis.mvpexample.ui.fingerprint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.recyclerview.adapter.FingerprintAdapter;
import com.asolis.mvpexample.recyclerview.models.FingerprintItem;
import com.asolis.mvpexample.ui.base.BaseFragment;
import com.asolis.mvpexample.ui.main.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;

public class FingerprintFragment extends BaseFragment<FingerprintFragmentPresenter, FingerprintFragmentView> implements FingerprintFragmentView {

    @Inject
    FingerprintFragmentPresenter mPresenter;

    @Bind(R.id.fragment_fingerprint_rv)
    RecyclerView mRecyclerView;

    public static String FRAGMENT_FINGERPRINT_TAG = "fingerprint";
    private FingerprintAdapter mFingerprintAdapter;

    public static FingerprintFragment newInstance() {
        return new FingerprintFragment();
    }

    @Override
    public FingerprintFragmentPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().onFetchFingerprints();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_manager, container, false);
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
    public void doFetchFingerprints() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getFingerprintCount();
    }

    @Override
    public void doShowError(String s) {
    }

    @Override
    public void doShowNetworkError() {
    }

    public void clearData() {
        mFingerprintAdapter.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fingerprint_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (item.getItemId()) {
            case R.id.action_fingerprint_add:
                // start fingerprint process
                mainActivity.startFingerprintEnroll();
                break;

            case R.id.action_fingerprint_delete_all:
                mainActivity.deleteAllFingerprints();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void prepareData(String count) {
        int totalCount = Integer.valueOf(count);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        ArrayList<FingerprintItem> mFingerprintItemList = new ArrayList<>();
        for (int i = 0; i < totalCount; i++) {
            FingerprintItem item = new FingerprintItem("Fingerprint " + i++, String.valueOf(i));
            mFingerprintItemList.add(item);
        }

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mFingerprintAdapter = new FingerprintAdapter(getContext(), mFingerprintItemList);
        mRecyclerView.setAdapter(mFingerprintAdapter);
    }


}
