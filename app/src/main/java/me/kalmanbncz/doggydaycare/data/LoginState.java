package me.kalmanbncz.doggydaycare.data;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public enum LoginState {
    LOGGED_OUT(0),
    LOGGING_IN(2),
    LOGGED_IN(1),
    LOGIN_ERROR(-1);

    private final int value;

    LoginState(int value) {
        this.value = value;
    }
}