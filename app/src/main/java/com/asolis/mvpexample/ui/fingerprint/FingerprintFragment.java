package com.asolis.mvpexample.ui.fingerprint;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.recyclerview.adapter.FingerprintAdapter;
import com.asolis.mvpexample.recyclerview.models.FingerprintItem;
import com.asolis.mvpexample.ui.base.BaseFragment;
import com.asolis.mvpexample.ui.main.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FingerprintFragment extends BaseFragment<FingerprintFragmentPresenter, FingerprintFragmentView> implements FingerprintFragmentView {

    @Inject
    FingerprintFragmentPresenter mPresenter;

    @Bind(R.id.fragment_fingerprint_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_fingerprint_ll)
    RelativeLayout mEmptyFingerprintView;
    Dialog mDialog;

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
        ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fingerprint_manager, container, false);
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
        doFetchFingerprints();
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

    public void prepareData(ArrayList<Integer> ids) {
        Log.e("prepareData","here");
        if (ids.size() > 0) {
            Log.e("ids","more than zero");
            mEmptyFingerprintView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);

            ArrayList<FingerprintItem> mFingerprintItemList = new ArrayList<>();

            for (int i = 0; i < ids.size(); i++) {
                FingerprintItem item = new FingerprintItem("Fingerprint " + ids.get(i), ids.get(i));
                mFingerprintItemList.add(item);
            }

            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setVerticalScrollBarEnabled(true);
            mFingerprintAdapter = new FingerprintAdapter(getContext(), mFingerprintItemList);
            mFingerprintAdapter.setOnclickListener(new FingerprintAdapter.OnclickListener() {
                @Override
                public void onClick(View view, final FingerprintItem item) {
                    Log.e("onClick","" + item.getEnrollId());
//                doShowFingerPrintOptions(item);
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.action_fingerprint_delete:
                                    doShowDeleteDialog(item);
                                    return true;
                            }
                            return false;
                        }
                    });
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.fingerprint_actions, popup.getMenu());
                    popup.show();
                }
            });
            mRecyclerView.setAdapter(mFingerprintAdapter);
        } else {
            Log.e("count is zero", "here");
            mRecyclerView.setVisibility(View.GONE);
            mEmptyFingerprintView.setVisibility(View.VISIBLE);
        }
    }

    private void doShowDeleteDialog(final FingerprintItem item) {
        mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_delete_fingerprint_confirmation);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout
                .LayoutParams.WRAP_CONTENT);
        TextView textView = (TextView) mDialog.findViewById(R.id.dialog_delete_fingerprint_title);
        textView.setText(getString(R.string.delete_fingerprint_dialog_title, item.getTitle()));
        Button dialogSaveButton = (Button) mDialog.findViewById(R.id.dialog_delete_fingerprint_yes_btn);
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("setOnClickListener","" + item.getEnrollId());
                mDialog.dismiss();
                onDeleteFingerprintClicked(item);
            }
        });
        Button dialogNoButton = (Button) mDialog.findViewById(R.id.dialog_delete_fingerprint_no_btn);
        dialogNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void onDeleteFingerprintClicked(FingerprintItem item) {
        // TODO: send delete command
        ((MainActivity) getActivity()).deleteFingerPrint(item);
    }

    public void deleteSuccessful() {
        Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
        doFetchFingerprints();
    }
}
