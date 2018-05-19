package me.kalmanbncz.doggydaycare.domain.user.api;

import io.reactivex.Observable;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class MockUserRetrofitApi implements UserRetrofitApi {

    private static final String MOCK_TOKEN = "mockRequestToken";

    private static final String MOCK_ACCESS_TOKEN = "mockAccessToken";

    @Override
    public Observable<LoginResponse> login(String apiKey, String requestToken, String username, String password) {
        LoginResponse response = new LoginResponse();
        response.success = username != null && password != null && !username.isEmpty() && !password.isEmpty() && requestToken != null &&
            !requestToken.isEmpty();
        response.requestToken = response.success ? MOCK_TOKEN : null;
        return Observable.just(response);
    }

    @Override
    public Observable<RequestTokenResponse> requestAccessToken(String apiKey) {
        RequestTokenResponse response = new RequestTokenResponse();
        response.success = apiKey != null && !apiKey.isEmpty();
        response.expiresAt = response.success ? "never" : null;
        response.requestToken = response.success ? MOCK_ACCESS_TOKEN : null;
        return Observable.just(response);
    }
}
