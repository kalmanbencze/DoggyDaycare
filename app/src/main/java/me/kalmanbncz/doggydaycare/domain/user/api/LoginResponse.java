package me.kalmanbncz.doggydaycare.domain.user.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class LoginResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("request_token")
    public String requestToken;
}
