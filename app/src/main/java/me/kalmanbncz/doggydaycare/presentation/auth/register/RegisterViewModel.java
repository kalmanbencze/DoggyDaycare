package me.kalmanbncz.doggydaycare.presentation.auth.register;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.di.scopes.screen.LoginScreenScope;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@LoginScreenScope
public class RegisterViewModel {

    //    private static final String USERNAME_REGEX = "/^.{4,}$/";
    //
    //    private static final String PASSWORD_REGEX = "/^.{4,}$/";

    private final UserRepository userRepository;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final ResourcesProvider resourcesProvider;

    public PublishSubject<String> snackbar = PublishSubject.create();

    private BehaviorSubject<String> usernameSubject = BehaviorSubject.create();

    private BehaviorSubject<String> passwordSubject = BehaviorSubject.create();

    @Inject
    RegisterViewModel(ResourcesProvider resourcesProvider, UserRepository userRepository) {
        this.resourcesProvider = resourcesProvider;
        this.userRepository = userRepository;
        title.onNext("Register");
    }

    public Observable<LoginState> getLoginState() {
        return userRepository.loadCurrentUser()
            .map(user -> user.isValid() ? LoginState.LOGGED_IN : LoginState.LOGGED_OUT);
    }

    public Observable<Boolean> validator() {
        return Observable.combineLatest(usernameSubject, passwordSubject, this::validate);
    }

    private Boolean validate(String user, String password) {
        //        boolean userMatches = Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), user) || Pattern.matches(USERNAME_REGEX, user);
        //        boolean passwordMatches = Pattern.matches(PASSWORD_REGEX, password);
        boolean userMatches = user.length() > 4;
        boolean passwordMatches = password.length() > 4;

        return userMatches && passwordMatches;
    }

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

    private LoginState mapLoginResult(LoginResult loginResult) {
        return loginResult.isSuccess() ? LoginState.LOGGED_IN : LoginState.LOGIN_ERROR;
    }

    public Observable<String> getTitle() {
        return title;
    }

    public BehaviorSubject<String> getPasswordSubject() {
        return passwordSubject;
    }

    public BehaviorSubject<String> getUsernameSubject() {
        return usernameSubject;
    }
}
