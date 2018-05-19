package me.kalmanbncz.doggydaycare.domain.dog.api;

import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class MockDogsRetrofitApi implements DogsRetrofitApi {

    @Override
    public Observable<DogsJSONResponse> getDogs(int page, String apiKey) {
        DogsJSONResponse response = new DogsJSONResponse();
        response.page = page;
        response.totalPages = 18;
        response.totalResults = 180;
        response.results = new ArrayList<>();
        if (page * 10 <= response.totalResults) {
            for (int i = (page - 1) * 10; i < (page) * 10; i++) {
                DogJSONResult result = new DogJSONResult();
                result.id = i;
                result.name = "name" + (i + 1);
                result.breed = "breed" + (i + 1);
                result.gender = i % 2 == 0 ? "male" : "female";
                result.neutered = new Random().nextBoolean();
                result.friendly = new Random().nextBoolean();
                result.vaccinated = new Random().nextBoolean();
                result.size = i % 2 == 0 ? "small" : "large";
                result.yearOfBirth = i % 2 == 0 ? "2014" : "2016";
                result.commands = i % 2 == 0 ? "Sit, stay." : "sit, stay, jump, low, roll.";
                result.walkSched = i % 2 == 0 ? "Daily in the evening." : "Every two days.";
                result.eatingSched = i % 2 == 0 ? "2 times a day, morning and afternoon." : "One daily, around 2:pm";
                result.sleepSched = i % 2 == 0 ? "All day." : "Naps around 12pm, and in the afternoon.";
                response.results.add(result);
            }
        }
        return Observable.just(response);
    }

    @Override
    public Observable<BreedsJSONResponse> getBreeds(String apiKey) {
        BreedsJSONResponse response = new BreedsJSONResponse();
        response.page = 1;
        response.results = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BreedJSONResponse result = new BreedJSONResponse();
            result.id = i;
            result.name = "breed" + (i + 1);
            response.results.add(result);
        }
        return Observable.just(response);
    }
}
