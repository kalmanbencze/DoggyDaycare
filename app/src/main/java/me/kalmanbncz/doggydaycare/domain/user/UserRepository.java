package me.kalmanbncz.doggydaycare.domain.user;

import io.reactivex.Observable;
import me.kalmanbncz.doggydaycare.AppScope;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.User;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@AppScope
public interface UserRepository {

    Observable<LoginResult> login(String username, String password);

    Observable<User> loadCurrentUser();

    void clearCurrentUser();
}
