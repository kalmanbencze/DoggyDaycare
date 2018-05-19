package me.kalmanbncz.doggydaycare.domain.user.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public interface UserRetrofitApi {

    String AUTHENTICATION_URL = "authentication/token/validate_with_login";

    String REQUEST_TOKEN_URL = "authentication/token/new";

    /**
     * Validate a given request token with the username and password.
     *
     * @param apiKey the api key of the app
     * @param requestToken the request token to validate
     * @param username the username of the user
     * @param password the password of the user
     *
     * @return a {@link Call} which in turn can return a {@link LoginResponse}
     */
    @GET(AUTHENTICATION_URL)
    Observable<LoginResponse> login(@Query("api_key") String apiKey,
                                    @Query("request_token") String requestToken,
                                    @Query("username") String username,
                                    @Query("password") String password);

    /**
     * Get a request token from the API.
     *
     * @param apiKey the api key of the app
     *
     * @return a {@link Call} which in turn can return a {@link RequestTokenResponse}
     */
    @GET(REQUEST_TOKEN_URL)
    Observable<RequestTokenResponse> requestAccessToken(@Query("api_key") String apiKey);
}
