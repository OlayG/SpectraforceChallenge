package com.example.olayg.spectraforcechallenge.view.mainactivity;

import com.example.olayg.spectraforcechallenge.component.NetComponent;
import com.example.olayg.spectraforcechallenge.util.CustomScope;

import dagger.Component;

/**
 * Created by olayg on 1/19/2018.
 */
@CustomScope
@Component(dependencies = {NetComponent.class}, modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
