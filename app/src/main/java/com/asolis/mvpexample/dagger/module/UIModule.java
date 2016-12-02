package com.asolis.mvpexample.dagger.module;

import com.asolis.mvpexample.ui.discover.DiscoverBLEActivityPresenter;
import com.asolis.mvpexample.ui.discover.DiscoverBLEActivityPresenterImpl;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragmentPresenter;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragmentPresenterImpl;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollActivityPresenter;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollActivityPresenterImpl;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollFragmentPresenter;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollFragmentPresenterImpl;
import com.asolis.mvpexample.ui.home.HomeFragmentPresenter;
import com.asolis.mvpexample.ui.home.HomeFragmentPresenterImpl;
import com.asolis.mvpexample.ui.lights.LightsFragmentPresenter;
import com.asolis.mvpexample.ui.lights.LightsFragmentPresenterImpl;
import com.asolis.mvpexample.ui.main.MainActivityPresenter;
import com.asolis.mvpexample.ui.main.MainActivityPresenterImpl;
import com.asolis.mvpexample.ui.splash.SplashActivityPresenter;
import com.asolis.mvpexample.ui.splash.SplashActivityPresenterImpl;

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

    @Provides
    @Singleton
    SplashActivityPresenter providesSplashActivityPresenter() {
        return new SplashActivityPresenterImpl();
    }

    @Provides
    @Singleton
    DiscoverBLEActivityPresenter providesBLEStartActivityPresenter() {
        return new DiscoverBLEActivityPresenterImpl();
    }

    @Provides
    @Singleton
    HomeFragmentPresenter providesHomeFragmentPresenter() {
        return new HomeFragmentPresenterImpl();
    }

    @Provides
    @Singleton
    FingerprintFragmentPresenter providesFingerprintFragmentPresenter() {
        return new FingerprintFragmentPresenterImpl();
    }

    @Provides
    @Singleton
    LightsFragmentPresenter providesLightsFragmentPresenter() {
        return new LightsFragmentPresenterImpl();
    }

    @Provides
    @Singleton
    FingerprintEnrollFragmentPresenter fingerprintEnrollFragmentPresenter() {
        return new FingerprintEnrollFragmentPresenterImpl();
    }

    @Provides
    @Singleton
    FingerprintEnrollActivityPresenter providesFingerprintEnrollActivityPresenter() {
        return new FingerprintEnrollActivityPresenterImpl();
    }
}
