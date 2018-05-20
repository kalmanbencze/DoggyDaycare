package me.kalmanbncz.doggydaycare.presentation.auth.login;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.kalmanbncz.doggydaycare.data.LoginState;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
interface LoginViewModel {

    Observable<LoginState> getLoginState();

    Observable<Boolean> validator();

    Completable logIn(String user, String password);

    Observable<String> getTitle();

    Observable<String> getSnackbar();

    BehaviorSubject<String> getPasswordSubject();

    BehaviorSubject<String> getUsernameSubject();
}
