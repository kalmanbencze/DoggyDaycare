package me.kalmanbncz.doggydaycare.di;

import me.kalmanbncz.doggydaycare.presentation.splash.SplashNavigator;
import me.kalmanbncz.doggydaycare.presentation.splash.SplashNavigatorImpl;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class SplashModule extends Module {

    public SplashModule() {
        bind(SplashNavigator.class).to(SplashNavigatorImpl.class).singletonInScope();
    }
}
