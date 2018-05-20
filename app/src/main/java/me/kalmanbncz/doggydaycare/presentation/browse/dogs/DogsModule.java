package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class DogsModule extends Module {

    public DogsModule() {
        bind(DogsViewModel.class).to(DogsViewModelImpl.class);
    }
}
