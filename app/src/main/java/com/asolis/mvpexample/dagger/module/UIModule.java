package com.asolis.mvpexample.dagger.module;

import com.asolis.mvpexample.ui.main.MainActivityPresenter;
import com.asolis.mvpexample.ui.main.MainActivityPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by angelsolis on 11/3/16.
 */

@Module
public class UIModule {
    @Provides
    @Singleton
    MainActivityPresenter providesMainActivityPresenter() {
        return new MainActivityPresenterImpl();
    }
}
