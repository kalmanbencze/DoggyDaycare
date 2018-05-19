package me.kalmanbncz.doggydaycare.data;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class LoginResult {

    private final boolean success;

    private final String requestToken;

    private final Throwable error;

    public LoginResult(String requestToken) {
        this.success = true;
        this.requestToken = requestToken;
        this.error = null;
    }

    public LoginResult(Throwable error) {
        this.success = false;
        this.requestToken = null;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public Throwable getError() {
        return error;
    }
}
