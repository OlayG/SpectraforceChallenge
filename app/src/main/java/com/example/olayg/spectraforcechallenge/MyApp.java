package com.example.olayg.spectraforcechallenge;

import android.app.Application;

import com.example.olayg.spectraforcechallenge.component.DaggerNetComponent;
import com.example.olayg.spectraforcechallenge.component.NetComponent;
import com.example.olayg.spectraforcechallenge.module.AppModule;
import com.example.olayg.spectraforcechallenge.module.NetModule;

import timber.log.Timber;

/**
 * Created by olayg on 1/19/2018.
 */

public class MyApp extends Application {
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        initDagger();
    }

    private void initDagger() {
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
