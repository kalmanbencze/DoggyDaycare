package me.kalmanbncz.doggydaycare.domain.dog.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The BackendApi's retrofit interface, used for generating the implementation class.
 *
 * Created by kalman.bencze on 18/05/2018.
 */
public interface DogsRetrofitApi {

    String DOG_URL = "dogs";

    String BREED_URL = "breeds";

    /**
     * Get the dogs at the given page.
     *
     * @param page the page at which to get the dogs
     * @param apiKey the api key of the api
     *
     * @return a {@link Call} object which will return the response {@link DogsJSONResponse}
     */
    @GET(DOG_URL)
    Observable<DogsJSONResponse> getDogs(@Query("page") int page,
                                         @Query("api_key") String apiKey);

    /**
     * Get the available breeds.
     *
     * @param apiKey the api key of the api
     *
     * @return a {@link Call} object which will return the response {@link BreedJSONResponse}
     */
    @GET(BREED_URL)
    Observable<BreedsJSONResponse> getBreeds(@Query("api_key") String apiKey);
}
