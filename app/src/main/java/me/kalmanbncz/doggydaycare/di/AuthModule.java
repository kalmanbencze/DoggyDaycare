package me.kalmanbncz.doggydaycare.di;

import me.kalmanbncz.doggydaycare.presentation.auth.AuthNavigator;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthNavigatorImpl;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class AuthModule extends Module {

    public AuthModule() {
        bind(AuthNavigator.class).to(AuthNavigatorImpl.class).singletonInScope();
    }
}
