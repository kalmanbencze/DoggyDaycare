package me.kalmanbncz.doggydaycare.domain.dog;

import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.DogsPageList;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.api.BreedJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.BreedsJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogJSONResult;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogsJSONResponse;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogsRetrofitApi;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.BreedEntity;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogDao;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogDatabase;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogEntity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseFlowScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@BrowseFlowScope
public class DogRepositoryImpl implements DogRepository {

    private static final String TAG = "DogRepositoryImpl";

    private final DogsRetrofitApi dogsRetrofitApi;

    private final String apiKey;

    private final DogDao dogDao;

    private ReplayProcessor<Integer> paginator = ReplayProcessor.create();

    private int pageIndex = 0;

    @Inject
    DogRepositoryImpl(DogsRetrofitApi dogsRetrofitApi, DogDatabase dogDao, ResourcesProvider resourcesProvider) {
        Log.d(TAG, "DogRepositoryImpl: created");
        this.dogsRetrofitApi = dogsRetrofitApi;
        this.dogDao = dogDao.dogDao();
        apiKey = resourcesProvider.getApiKey();
    }

    /**
     * Discover dogs at a certain page.
     *
     * @return a {@link DogsPageList} with a list of dogs
     */
    public Flowable<List<Dog>> getDogs() {
        Log.d(TAG, "getDogs: ");
        return dogDao.getDogs()
            .map(this::mapToDogs);
        // TODO: 5/20/2018 enable this for joint results from server
        //return Flowable.concat(dogDao.getDogs()
        //                             .map(this::mapToDogs),
        //                         retrieveDogsInternal()
        //                             .map(this::mapToDogs));
    }

    /**
     * Make an API call for the available breeds.
     */
    @Override
    public Observable<List<Breed>> getBreeds() {
        Log.d(TAG, "getBreeds: ");
        return Observable.concat(dogDao.getBreeds()
                                     .toObservable()
                                     .map(this::mapTBreeds),
                                 dogsRetrofitApi.getBreeds(apiKey)
                                     .subscribeOn(Schedulers.io())
                                     .map(this::mapTBreeds));
    }

    @Override
    public Observable<OperationStatus> addOrUpdate(Dog dog) {
        if (!dog.isValid()) {
            return BehaviorSubject.createDefault(new OperationStatus(OperationStatus.FAILED));
        }
        BehaviorSubject<OperationStatus> res = BehaviorSubject.createDefault(new OperationStatus(OperationStatus.STARTED));
        return res.map(status -> {
            long nr = dogDao.addOrUpdateDog(DogConverter.toEntity(dog));
            if (nr > 0) {
                return new OperationStatus(OperationStatus.LOCAL_SUCCESS);
            } else {
                return new OperationStatus(OperationStatus.FAILED);
            }
        }).flatMap((status) -> {
            if (status.isSuccess()) {
                return dogsRetrofitApi.addOrUpdateDog(String.valueOf(dog.getId()), DogConverter.toJsonObject(dog), apiKey)
                    .map(insertJsonResponse ->
                             new OperationStatus(insertJsonResponse.code == 200 ? OperationStatus.ONLINE_SUCCESS : OperationStatus.FAILED));
            } else {
                return Observable.just(new OperationStatus(OperationStatus.FAILED));
            }
        });
    }

    @Override
    public void loadMoreDogs() {
        pageIndex++;
        Log.d(TAG, "loadMoreItems: requesting page " + pageIndex);
        paginator.onNext(pageIndex);
    }

    /**
     * Get dogs at a certain page.
     *
     * @return a {@link DogsPageList} with a list of dogs
     */
    private Flowable<DogsPageList> retrieveDogsInternal() {
        return paginator.subscribeOn(Schedulers.computation())
            .map(page -> {
                //loading.onNext(true);
                DogsPageList first = dogsRetrofitApi.getDogs(page, apiKey).map(this::mapToDogs)
                    //.doOnNext(dogs -> loading.onNext(false))
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .blockingFirst();
                return first;
            })
            .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Dog> mapToDogs(DogsPageList dogsPageList) {
        return dogsPageList.getDogs();
    }

    private List<Breed> mapTBreeds(BreedsJSONResponse breedsJSONResponse) {
        Log.d(TAG, "mapTBreeds: from server");
        final List<Breed> breeds = new ArrayList<>();
        for (BreedJSONResponse breedJSONResult : breedsJSONResponse.results) {
            breeds.add(new Breed(breedJSONResult.id, breedJSONResult.name));
        }
        return breeds;
    }

    private DogsPageList mapToDogs(DogsJSONResponse dogsJSONResponse) {
        Log.d(TAG, "mapToDogs: from server");
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

    private List<Breed> mapTBreeds(List<BreedEntity> breedEntities) {
        Log.d(TAG, "mapTBreeds: from DB");
        final List<Breed> breeds = new ArrayList<>();
        for (BreedEntity breedEntity : breedEntities) {
            breeds.add(new Breed(breedEntity.id, breedEntity.name));
        }
        return breeds;
    }

    private List<Dog> mapToDogs(List<DogEntity> dogEntities) {
        Log.d(TAG, "mapToDogs: from DB");
        final List<Dog> dogs = new ArrayList<>();
        for (DogEntity dogEntity : dogEntities) {
            final Dog dog = new Dog(dogEntity.id,
                                    dogEntity.name,
                                    dogEntity.breed,
                                    dogEntity.gender,
                                    dogEntity.size,
                                    dogEntity.yearOfBirth,
                                    dogEntity.vaccinated,
                                    dogEntity.neutered,
                                    dogEntity.friendly,
                                    dogEntity.commands,
                                    dogEntity.eatingSched,
                                    dogEntity.sleepSched,
                                    dogEntity.walkSched);
            dogs.add(dog);
        }
        return dogs;
    }

    private class DogsRetrievalFailed extends Throwable {

        private Throwable throwable;

        DogsRetrievalFailed(Throwable throwable) {
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
