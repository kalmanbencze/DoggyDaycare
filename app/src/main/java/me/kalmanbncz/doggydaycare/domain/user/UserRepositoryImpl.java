package me.kalmanbncz.doggydaycare.domain.user;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.AppScope;
import me.kalmanbncz.doggydaycare.data.LoginResult;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.user.api.LoginResponse;
import me.kalmanbncz.doggydaycare.domain.user.api.UserRetrofitApi;
import me.kalmanbncz.doggydaycare.domain.user.persistance.UserCache;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@AppScope
public class UserRepositoryImpl implements UserRepository {

    private static final String TAG = "UserRepositoryImpl";

    private final UserRetrofitApi userRetrofitApi;

    private final UserCache userCache;

    private final String apiKey;

    @Inject
    UserRepositoryImpl(UserCache userCache, UserRetrofitApi userRetrofitApi, ResourcesProvider resourcesProvider) {
        Log.d(TAG, "UserRepositoryImpl: created");
        this.userCache = userCache;
        this.userRetrofitApi = userRetrofitApi;
        this.apiKey = resourcesProvider.getApiKey();
    }

    /**
     * Login the current user by requesting a token and then validating it with the given
     * username and password.
     *
     * @param username the username to validate with
     * @param password the password to validate with
     *
     * @return a {@link Observable} that emits a {@link LoginResult} when the validation is complete.
     */
    @Override
    public Observable<LoginResult> login(String username, String password) {
        return userRetrofitApi.requestAccessToken(apiKey)
            .subscribeOn(Schedulers.io())
            .flatMap(requestTokenResponse -> {
                return userRetrofitApi.login(apiKey, requestTokenResponse.requestToken, username, password)
                    .map(UserRepositoryImpl.this::mapLoginResponse)
                    .doOnError(throwable -> userCache.removeCurrentUser())
                    .doOnNext(loginResult -> {
                        if (loginResult.isSuccess()) {
                            userCache.setCurrentUser(
                                new User(0, username, requestTokenResponse.requestToken));//todo hardcoded id 0 for a mock user
                        } else {
                            userCache.removeCurrentUser();
                        }
                    })
                    .onErrorReturn(throwable -> new LoginResult(new LoginFailed(throwable)));
            }).doOnError(throwable -> userCache.removeCurrentUser())
            .onErrorReturn(throwable -> new LoginResult(new RequestTokenFailed(throwable)));
    }

    /**
     * Loads the current user saved in local storage.
     *
     * @return a {@link Observable} which in turn emits an {@link User}
     */
    @Override
    public Observable<User> loadCurrentUser() {
        return userCache.getCurrentUser();
    }

    /**
     * Clear the current user from local storage.
     */
    @Override
    public void clearCurrentUser() {
        userCache.removeCurrentUser();
    }

    private LoginResult mapLoginResponse(LoginResponse loginResponse) {
        return new LoginResult(loginResponse.requestToken);
    }

    private class RequestTokenFailed extends Throwable {

        private final Throwable throwable;

        RequestTokenFailed(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    private class LoginFailed extends Throwable {

        private final Throwable throwable;

        LoginFailed(Throwable code) {
            this.throwable = code;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
