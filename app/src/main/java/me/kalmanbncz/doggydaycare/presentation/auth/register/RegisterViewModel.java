package me.kalmanbncz.doggydaycare.presentation.auth.register;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.di.scopes.screen.LoginScreenScope;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;
import me.kalmanbncz.doggydaycare.presentation.BaseViewModel;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@LoginScreenScope
public class RegisterViewModel implements BaseViewModel {

    //    private static final String USERNAME_REGEX = "/^.{4,}$/";
    //
    //    private static final String PASSWORD_REGEX = "/^.{4,}$/";

    private final UserRepository userRepository;

    private final BehaviorSubject<LoginState> state = BehaviorSubject.create();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private BehaviorSubject<String> usernameSubject = BehaviorSubject.create();

    private BehaviorSubject<String> passwordSubject = BehaviorSubject.create();

    @Inject
    RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        state.onNext(LoginState.LOGGED_OUT);
        title.onNext("Login");
    }

    @Override
    public void onAttach() {
        subscriptions.clear();
        subscriptions.add(userRepository.loadCurrentUser().subscribe(user -> {
            if (user.isValid()) {
                state.onNext(LoginState.LOGGED_IN);
            } else {
                state.onNext(LoginState.LOGGED_OUT);
            }
        }));
    }

    @Override
    public void onDetach() {
        subscriptions.clear();
    }

    public Observable<LoginState> getLoginState() {
        return state;
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

    public void logIn(String user, String password) {
        state.onNext(LoginState.LOGGING_IN);
        subscriptions.add(userRepository.login(user, password).map(this::mapLoginResult).subscribe(state::onNext));
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