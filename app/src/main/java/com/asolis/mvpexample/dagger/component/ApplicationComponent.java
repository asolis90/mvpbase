package com.asolis.mvpexample.dagger.component;

import com.asolis.mvpexample.dagger.module.ApplicationModule;
import com.asolis.mvpexample.dagger.module.UIModule;
import com.asolis.mvpexample.ui.discover.DiscoverBLEActivity;
import com.asolis.mvpexample.ui.fingerprint.FingerprintFragment;
import com.asolis.mvpexample.ui.fingerprintenroll.FingerprintEnrollFragment;
import com.asolis.mvpexample.ui.home.HomeFragment;
import com.asolis.mvpexample.ui.lights.LightsFragment;
import com.asolis.mvpexample.ui.main.MainActivity;
import com.asolis.mvpexample.ui.splash.SplashActivity;

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
    void inject(SplashActivity splashActivity);
    void inject(DiscoverBLEActivity discoverBLEActivity);
    void inject(HomeFragment homeFragment);
    void inject(LightsFragment lightsFragment);
    void inject(FingerprintFragment fingerprintFragment);
    void inject(FingerprintEnrollFragment fingerprintEnrollFragment);
}