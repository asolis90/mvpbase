package com.asolis.mvpexample.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.ui.base.BaseFragment;
import javax.inject.Inject;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter, HomeFragmentView> implements HomeFragmentView {

    @Inject
    HomeFragmentPresenter mPresenter;

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
}
