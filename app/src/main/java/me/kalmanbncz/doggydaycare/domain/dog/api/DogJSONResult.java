package me.kalmanbncz.doggydaycare.domain.dog.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DogJSONResult {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("breed")
    public String breed;

    @SerializedName("gender")
    public String gender;

    @SerializedName("yearOfBirth")
    public String yearOfBirth;

    @SerializedName("size")
    public String size;

    @SerializedName("vaccinated")
    public boolean vaccinated;

    @SerializedName("neutered")
    public boolean neutered;

    @SerializedName("friendly")
    public boolean friendly;

    @SerializedName("commands")
    public String commands;

    @SerializedName("eatingSched")
    public String eatingSched;

    @SerializedName("walkSched")
    public String walkSched;

    @SerializedName("sleepSched")
    public String sleepSched;
}
