package me.kalmanbncz.doggydaycare.domain.dog.api;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    @GET("{user_id}/" + DOG_URL)
    Observable<DogsJSONResponse> getDogs(@Path("user_id") int userId, @Query("page") int page,
                                         @Query("access_token") String accessToken, @Query("api_key") String apiKey);

    /**
     * Get the available breeds.
     *
     * @param apiKey the api key of the api
     *
     * @return a {@link Call} object which will return the response {@link BreedJSONResponse}
     */
    @GET(BREED_URL)
    Observable<BreedsJSONResponse> getBreeds(@Query("api_key") String apiKey);

    /**
     * Add or update a dog
     *
     * @param apiKey the api key of the api
     *
     * @return a {@link Completable} object which will return the status of the operation
     */
    @PUT(DOG_URL + "/{id}")
    Observable<InsertJsonResponse> addOrUpdateDog(@Path("id") String id, @Body DogJSONResult dogJSONResult,
                                                  @Query("api_key") String apiKey);
}
