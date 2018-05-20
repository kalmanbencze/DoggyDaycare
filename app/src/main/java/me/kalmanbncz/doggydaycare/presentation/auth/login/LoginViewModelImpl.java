package me.kalmanbncz.doggydaycare.presentation.auth.login;

import android.util.Log;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@LoginScreenScope
class LoginViewModelImpl implements LoginViewModel {

    private static final String TAG = LoginViewModelImpl.class.getSimpleName();

    //    private static final String USERNAME_REGEX = "/^.{4,}$/";
    //
    //    private static final String PASSWORD_REGEX = "/^.{4,}$/";

    private final UserRepository userRepository;

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final ResourcesProvider resourcesProvider;

    private final PublishSubject<String> snackbar = PublishSubject.create();

    private final BehaviorSubject<String> usernameSubject = BehaviorSubject.create();

    private final BehaviorSubject<String> passwordSubject = BehaviorSubject.create();

    @Inject
    LoginViewModelImpl(ResourcesProvider resourcesProvider, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.resourcesProvider = resourcesProvider;
        title.onNext(resourcesProvider.getLoginScreenTitle());
    }

    @Override
    public Observable<LoginState> getLoginState() {
        return userRepository.loadCurrentUser()
            .map(user -> user.isValid() ? LoginState.LOGGED_IN : LoginState.LOGGED_OUT);
    }

    @Override
    public Observable<Boolean> validator() {
        return Observable.combineLatest(usernameSubject, passwordSubject, this::validate);
    }

    @Override
    public Completable logIn(String user, String password) {
        return userRepository.login(user, password)
            .flatMapCompletable(loginResult -> {
                if (loginResult.isSuccess()) {
                    return Completable.complete();
                } else {
                    return Completable.error(loginResult.getError());
                }
            })
            .doOnComplete(() -> snackbar.onNext(resourcesProvider.getWelcomeMessage(user)))
            .doOnError(throwable -> snackbar.onNext(resourcesProvider.getLoginErrorMessage()));
    }

    @Override
    public Observable<String> getTitle() {
        return title;
    }

    @Override
    public Observable<String> getSnackbar() {
        return snackbar;
    }

    @Override
    public BehaviorSubject<String> getPasswordSubject() {
        return passwordSubject;
    }

    @Override
    public BehaviorSubject<String> getUsernameSubject() {
        return usernameSubject;
    }

    private Boolean validate(String user, String password) {
        //        boolean userMatches = Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), user) || Pattern.matches(USERNAME_REGEX, user);
        //        boolean passwordMatches = Pattern.matches(PASSWORD_REGEX, password);
        boolean userMatches = user.length() > 4;
        boolean passwordMatches = password.length() > 4;

        return userMatches && passwordMatches;
    }

    private void loggedIn(String user, LoginResult loginResult) {
        Log.d(TAG, "loggedIn: success = " + loginResult.isSuccess());
        if (loginResult.isSuccess()) {
            snackbar.onNext(resourcesProvider.getWelcomeMessage(user));
        } else {
            snackbar.onNext(resourcesProvider.getLoginErrorMessage());
        }
    }
}
