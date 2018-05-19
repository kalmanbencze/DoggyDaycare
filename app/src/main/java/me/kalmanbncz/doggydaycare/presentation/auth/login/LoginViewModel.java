package me.kalmanbncz.doggydaycare.presentation.auth.login;

import android.util.Log;
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
import me.kalmanbncz.doggydaycare.presentation.BaseViewModel;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@LoginScreenScope
public class LoginViewModel implements BaseViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();

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
    LoginViewModel(ResourcesProvider resourcesProvider, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.resourcesProvider = resourcesProvider;
        title.onNext(resourcesProvider.getLoginScreenTitle());
    }

    @Override
    public void onAttach() {
    }

    @Override
    public void onDetach() {
        subscriptions.clear();
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

    public void logIn(String user, String password) {
        subscriptions.add(userRepository.login(user, password).subscribe(loginResult -> {
            loggedIn(user, loginResult);
        }));
    }

    private void loggedIn(String user, LoginResult loginResult) {
        Log.d(TAG, "loggedIn: success = " + loginResult.isSuccess());
        if (loginResult.isSuccess()) {
            snackbar.onNext(resourcesProvider.getWelcomeMessage(user));
        } else {
            snackbar.onNext(resourcesProvider.getLoginErrorMessage());
        }
    }

    public Observable<String> getTitle() {
        return title;
    }

    public Observable<String> getSnackbar() {
        return snackbar;
    }

    public BehaviorSubject<String> getPasswordSubject() {
        return passwordSubject;
    }

    public BehaviorSubject<String> getUsernameSubject() {
        return usernameSubject;
    }
}
