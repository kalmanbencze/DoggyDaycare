package me.kalmanbncz.doggydaycare.domain.user.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class RequestTokenResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("expires_at")
    public String expiresAt;

    @SerializedName("request_token")
    public String requestToken;
}
