package me.kalmanbncz.doggydaycare.presentation.auth.register;

import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class RegisterModule extends Module {

    public RegisterModule() {
        bind(RegisterViewModel.class).to(RegisterViewModelImpl.class);
    }
}
