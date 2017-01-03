package com.asolis.mvpexample.ui.discover.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.asolis.mvpexample.R;
import com.asolis.mvpexample.ui.base.blescan.BaseBLEStartActivity;

import butterknife.Bind;

/**
 * Created by angelsolis on 11/20/16.
 */

public class PagesAdapter extends PagerAdapter {

    private final Context mContext;
    private BaseBLEStartActivity.PagerCallback mCallback;

    public PagesAdapter(Context context, BaseBLEStartActivity.PagerCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ViewGroup layout = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Log.e("position", "" + position);
        switch (position) {
            case 0:
                layout = (ViewGroup) inflater.inflate(R.layout.fragment_waking_ble, collection, false);
                Button button = (Button) layout.findViewById(R.id.next_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onClick(v);
                    }
                });
                break;
            case 1:
                layout = (ViewGroup) inflater.inflate(R.layout.fragment_ble_scan, collection, false);
                break;

        }
        if (layout != null) {
            collection.addView(layout);
        }
        return layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}