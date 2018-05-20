package me.kalmanbncz.doggydaycare.presentation.splash;

import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class SplashModule extends Module {

    public SplashModule() {
        bind(SplashNavigator.class).to(SplashNavigatorImpl.class).singletonInScope();
        bind(SplashViewModel.class).to(SplashViewModelImpl.class).singletonInScope();
    }
}
