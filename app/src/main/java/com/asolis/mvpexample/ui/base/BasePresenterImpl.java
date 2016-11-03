package com.asolis.mvpexample.ui.base;

import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by angelsolis on 11/3/16.
 */

public abstract class BasePresenterImpl<View extends BaseView> implements BasePresenter<View>
{
    private WeakReference<View> mViewWeakReference;

    @Override
    public void setView(View view)
    {
        mViewWeakReference = new WeakReference<View>(view);
    }

    @Override
    public View getView()
    {
        return mViewWeakReference.get();
    }
}
