package com.asolis.mvpexample.dagger.component;

import com.asolis.mvpexample.dagger.module.ApplicationModule;
import com.asolis.mvpexample.dagger.module.UIModule;
import com.asolis.mvpexample.ui.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by angelsolis on 11/3/16.
 */
@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                UIModule.class
        })
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}