package me.kalmanbncz.doggydaycare.domain.dog.api;

import io.reactivex.Observable;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class MockDogsRetrofitApi implements DogsRetrofitApi {

    @Override
    public Observable<DogsJSONResponse> getDogs(int page, String apiKey) {
        DogsJSONResponse response = new DogsJSONResponse();
        return Observable.just(response);
    }

    @Override
    public Observable<BreedsJSONResponse> getBreeds(int page, String apiKey) {
        BreedsJSONResponse response = new BreedsJSONResponse();
        return Observable.just(response);
    }
}
