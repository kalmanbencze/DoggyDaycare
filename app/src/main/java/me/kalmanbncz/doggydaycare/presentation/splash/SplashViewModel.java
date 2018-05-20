package me.kalmanbncz.doggydaycare.presentation.splash;

import io.reactivex.Observable;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
interface SplashViewModel {

    Observable<Boolean> loggedIn();
}
