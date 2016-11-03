package com.asolis.mvpexample.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by angelsolis on 11/3/16.
 */

@Module
public class ApplicationModule {
    private Context mContext;

    public ApplicationModule(Context context)
    {
        mContext = context;
    }

    @Provides
    @Singleton
    Context providesContext()
    {
        return mContext;
    }
}
