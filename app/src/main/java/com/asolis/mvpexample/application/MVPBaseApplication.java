package com.asolis.mvpexample.application;

import android.app.Application;
import android.content.Context;

import com.asolis.mvpexample.dagger.component.ApplicationComponent;
import com.asolis.mvpexample.dagger.component.DaggerApplicationComponent;
import com.asolis.mvpexample.dagger.module.ApplicationModule;
import com.asolis.mvpexample.dagger.module.UIModule;

/**
 * Created by angelsolis on 11/3/16.
 */

public class MVPBaseApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        buildComponent();
    }

    private void buildComponent() {

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .uIModule(new UIModule())
                .build();
    }

    public static MVPBaseApplication get(Context context) {
        return (MVPBaseApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }
}
