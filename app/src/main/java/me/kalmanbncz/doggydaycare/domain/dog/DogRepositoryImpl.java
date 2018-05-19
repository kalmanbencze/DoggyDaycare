package me.kalmanbncz.doggydaycare.domain.dog;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.DogsPageList;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.api.BreedJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.BreedsJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogJSONResult;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogsJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogsRetrofitApi;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@BrowseFlowScope
public class DogRepositoryImpl implements DogRepository {

    private static final String TAG = "DogRepositoryImpl";

    private final DogsRetrofitApi dogsRetrofitApi;

    private final String apiKey;

    @Inject
    DogRepositoryImpl(DogsRetrofitApi dogsRetrofitApi, ResourcesProvider resourcesProvider) {
        Log.d(TAG, "DogRepositoryImpl: created");
        this.dogsRetrofitApi = dogsRetrofitApi;
        apiKey = resourcesProvider.getApiKey();
    }

    /**
     * Discover dogs at a certain page.
     *
     * @return a {@link DogsPageList} with a list of dogs
     */
    private Observable<DogsPageList> discoverDogsInternal(int page) {
        return dogsRetrofitApi.getDogs(page, apiKey)
            .subscribeOn(Schedulers.io())
            .map(this::mapToDogs)
            .onErrorReturn(throwable -> new DogsPageList(new DogDiscoveryFailed(throwable)));
    }

    /**
     * Discover dogs at a certain page.
     *
     * @return a {@link DogsPageList} with a list of dogs
     */
    public Observable<List<Dog>> getDogs(int page) {
        Log.d(TAG, "getBreeds: ");
        return discoverDogsInternal(page).flatMap(this::convertDogPageResponseToDogsList);
    }

    /**
     * Make an API call for the details of the dog identified by {@code dogId}.
     *
     * @param page the id of the dog
     */
    @Override
    public Observable<List<Breed>> getBreeds(int page) {
        return dogsRetrofitApi.getBreeds(page, apiKey)
            .subscribeOn(Schedulers.io())
            .map(this::mapTBreed)
            .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void addOrUpdate(Dog dog) {

    }

    private Observable<List<Dog>> convertDogPageResponseToDogsList(DogsPageList dogsPageList) {
        Log.d(TAG, "convertDogPageResponseToDogsList: ");
        Observable<List<Dog>> res = Observable.just(dogsPageList.getDogs());
        Log.d(TAG, "convertDogPageResponseToDogsList: after just()");
        return res;
    }

    private List<Breed> mapTBreed(BreedsJSONResponse breedsJSONResponse) {
        Log.d(TAG, "mapTBreed: ");
        final List<Breed> genres = new ArrayList<>();
        for (BreedJSONResponse breedJSONResult : breedsJSONResponse.results) {
            genres.add(new Breed(breedJSONResult.id, breedJSONResult.name));
        }
        return genres;
    }

    private DogsPageList mapToDogs(DogsJSONResponse dogsJSONResponse) {
        final List<Dog> dogs = new ArrayList<>();
        for (DogJSONResult dogJSONResult : dogsJSONResponse.results) {
            final Dog dog = new Dog(dogJSONResult.id,
                                    dogJSONResult.name,
                                    dogJSONResult.breed,
                                    dogJSONResult.gender,
                                    dogJSONResult.size,
                                    dogJSONResult.yearOfBirth,
                                    dogJSONResult.vaccinated,
                                    dogJSONResult.neutered,
                                    dogJSONResult.friendly,
                                    dogJSONResult.commands,
                                    dogJSONResult.eatingSched,
                                    dogJSONResult.sleepSched,
                                    dogJSONResult.walkSched);
            dogs.add(dog);
        }
        return new DogsPageList(dogsJSONResponse.page,
                                dogsJSONResponse.totalPages,
                                dogsJSONResponse.totalResults,
                                dogs);
    }

    private class DogDiscoveryFailed extends Throwable {

        private Throwable throwable;

        DogDiscoveryFailed(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    private class SimilarDogsException extends Throwable {

        private Throwable throwable;

        SimilarDogsException(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    private class DogEditError extends Throwable {

        private Throwable throwable;

        DogEditError(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
