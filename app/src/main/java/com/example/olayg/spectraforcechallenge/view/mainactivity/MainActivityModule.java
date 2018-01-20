package com.example.olayg.spectraforcechallenge.view.mainactivity;

import com.example.olayg.spectraforcechallenge.util.CustomScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by olayg on 1/19/2018.
 */

@Module
public class MainActivityModule {
    private final MainActivityContract.View view;

    public MainActivityModule(MainActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @CustomScope
    MainActivityContract.View providesMainActivityContractView() {
        return view;
    }
}
