package com.asolis.mvpexample.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.asolis.mvpexample.application.MVPBaseApplication;
import com.asolis.mvpexample.dagger.component.ApplicationComponent;

public abstract class BaseFragment<Presenter extends BasePresenter<View>, View extends BaseView>
        extends Fragment
{
    public abstract void injectSelf(ApplicationComponent applicationComponent);

    public abstract Presenter getPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        injectSelf(MVPBaseApplication.get(getActivity()).getApplicationComponent());
        getPresenter().setView((View) this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getPresenter().setView(null);
    }

    /**
     * Sets the App Bar title from the supplied String resource ID
     */
    protected void setAppBarTitle(int titleResID)
    {
        String title = getString(titleResID);
        setAppBarTitle(title);
    }

    /**
     * Sets the App Bar title from the supplied String
     */
    protected void setAppBarTitle(String title)
    {
        FragmentActivity currentActivity = getActivity();
        if(currentActivity instanceof AppCompatActivity)
        {
            AppCompatActivity currentAppCompatActivity = (AppCompatActivity) currentActivity;
            ActionBar ab = currentAppCompatActivity.getSupportActionBar();
            if(ab != null)
            {
                ab.setTitle(title);
            }
        }
    }
}
