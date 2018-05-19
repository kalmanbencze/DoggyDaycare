package me.kalmanbncz.doggydaycare.domain.user.persistance;

import io.reactivex.Observable;
import me.kalmanbncz.doggydaycare.data.User;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public interface UserCache {

    Observable<User> getCurrentUser();

    void setCurrentUser(User user);

    void removeCurrentUser();
}
