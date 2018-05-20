package me.kalmanbncz.doggydaycare.presentation.auth.register;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import me.kalmanbncz.doggydaycare.data.LoginState;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
interface RegisterViewModel {

    Observable<LoginState> getLoginState();

    Observable<Boolean> validator();

    Completable logIn(String user, String password);

    Observable<String> getTitle();

    BehaviorSubject<String> getPasswordSubject();

    BehaviorSubject<String> getUsernameSubject();
}
