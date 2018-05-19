package me.kalmanbncz.doggydaycare.domain.dog.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BreedJSONResponse {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;
}
