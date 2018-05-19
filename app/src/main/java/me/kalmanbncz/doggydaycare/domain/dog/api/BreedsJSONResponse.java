package me.kalmanbncz.doggydaycare.domain.dog.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BreedsJSONResponse {

    @SerializedName("page")
    public int page;

    @SerializedName("total_results")
    public int totalResults;

    @SerializedName("total_pages")
    public int totalPages;

    @SerializedName("results")
    public List<BreedJSONResponse> results;
}
