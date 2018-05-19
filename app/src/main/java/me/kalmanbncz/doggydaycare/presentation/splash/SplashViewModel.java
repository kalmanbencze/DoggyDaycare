package me.kalmanbncz.doggydaycare.presentation.splash;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class SplashViewModel {

    private static final long SPLASH_DELAY = 1000;

    private final UserRepository userRepository;

    @Inject
    public SplashViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Observable<Boolean> loggedIn() {
        return userRepository.loadCurrentUser().map(this::isValid).delay(SPLASH_DELAY, TimeUnit.MILLISECONDS);
    }

    private Boolean isValid(User user) {
        return user.isValid();
    }
}
