package com.example.olayg.spectraforcechallenge.component;

import android.content.SharedPreferences;

import com.example.olayg.spectraforcechallenge.module.AppModule;
import com.example.olayg.spectraforcechallenge.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by olayg on 1/19/2018.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    SharedPreferences preferences();
}
