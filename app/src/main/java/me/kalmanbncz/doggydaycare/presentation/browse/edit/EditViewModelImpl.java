package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.domain.dog.OperationStatus;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@EditScreenScope
class EditViewModelImpl implements EditViewModel {

    private final DogRepository dogRepository;

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final BehaviorSubject<String> snackbar = BehaviorSubject.create();

    private final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);

    private final Observable<Dog> dogObservable;

    private Dog dog;

    @Inject
    EditViewModelImpl(ResourcesProvider resourcesProvider, DogRepository dogRepository, Dog dog) {
        this.dogRepository = dogRepository;
        this.dog = dog;
        this.dogObservable = Observable.just(dog);
        title.onNext(dog.getId() < 0 ? resourcesProvider.getCreateScreenTitle() : resourcesProvider.getEditScreenTitle());
    }

    @Override
    public Observable<DogAndBreedsHolder> getDogAndBreedsHolder() {
        return Observable.combineLatest(dogObservable, getBreeds(), DogAndBreedsHolder::new);
    }

    @Override
    public Observable<OperationStatus> save() {
        return dogRepository.addOrUpdate(dog);
    }

    @Override
    public Observable<String> getTitle() {
        return title;
    }

    @Override
    public Observable<String> getSnackbar() {
        return snackbar;
    }

    @Override
    public Observable<Boolean> getLoading() {
        return loading;
    }

    //public void updateDogFields(String name, String breed, int yearOfBirth, String size, boolean vaccinated, boolean neutered,
    //                            boolean friendly, String gender, String commands, String eating, String walking, String sleeping) {
    //    dog.setName(name);
    //    dog.setBreed(breed);
    //    dog.setYearOfBirth(String.valueOf(yearOfBirth));
    //    dog.setVaccinated(vaccinated);
    //    dog.setNeutered(neutered);
    //    dog.setFriendly(friendly);
    //    dog.setGender(gender);
    //    dog.setCommands(commands);
    //    dog.setEatingSched(eating);
    //    dog.setWalkSched(walking);
    //    dog.setSleepSched(sleeping);
    //}

    @Override
    public Observable<List<Breed>> getBreeds() {
        return dogRepository.getBreeds();
    }
}
