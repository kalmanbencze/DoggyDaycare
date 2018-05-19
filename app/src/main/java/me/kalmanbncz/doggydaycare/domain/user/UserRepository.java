package me.kalmanbncz.doggydaycare.domain.user;

import io.reactivex.Observable;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@ApplicationScope
public interface UserRepository {

    Observable<LoginResult> login(String username, String password);

    Observable<User> loadCurrentUser();

    void clearCurrentUser();
}
