package me.kalmanbncz.doggydaycare.presentation.auth.login;

import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class LoginModule extends Module {

    public LoginModule() {
        bind(LoginViewModel.class).to(LoginViewModelImpl.class);
    }
}
