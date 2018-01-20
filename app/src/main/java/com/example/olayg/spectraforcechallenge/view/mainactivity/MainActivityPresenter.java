package com.example.olayg.spectraforcechallenge.view.mainactivity;

import javax.inject.Inject;

/**
 * Created by olayg on 1/19/2018.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    MainActivityContract.View view;

    @Inject
    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
    }
}
