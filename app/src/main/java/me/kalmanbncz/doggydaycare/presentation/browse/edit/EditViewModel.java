package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.di.scopes.screen.EditScreenScope;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.presentation.BaseViewModel;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@EditScreenScope
public class EditViewModel implements BaseViewModel {

    private final DogRepository dogRepository;

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final BehaviorSubject<String> snackbar = BehaviorSubject.create();

    private final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);

    private final Observable<Dog> dogObservable;

    private Dog dog;

    @Inject
    EditViewModel(ResourcesProvider resourcesProvider, DogRepository dogRepository, Dog dog) {
        this.dogRepository = dogRepository;
        this.dog = dog;
        this.dogObservable = Observable.just(dog);
        title.onNext(dog.getId() < 0 ? resourcesProvider.getCreateScreenTitle() : resourcesProvider.getEditScreenTitle());
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    public Observable<DogAndBreedsHolder> getDogBreedsHolder() {
        return Observable.combineLatest(dogObservable, getBreeds(), DogAndBreedsHolder::new);
    }

    public Completable save() {
        return dogRepository.addOrUpdate(dog);
    }

    public Observable<String> getTitle() {
        return title;
    }

    public Observable<String> getSnackbar() {
        return snackbar;
    }

    public Observable<Boolean> getLoading() {
        return loading;
    }

    public void updateDogFields(String name, String breed, int yearOfBirth, String size, boolean vaccinated, boolean neutered,
                                boolean friendly, String gender, String commands, String eating, String walking, String sleeping) {
        dog.setName(name);
        dog.setBreed(breed);
        dog.setYearOfBirth(String.valueOf(yearOfBirth));
        dog.setVaccinated(vaccinated);
        dog.setNeutered(neutered);
        dog.setFriendly(friendly);
        dog.setGender(gender);
        dog.setCommands(commands);
        dog.setEatingSched(eating);
        dog.setWalkSched(walking);
        dog.setSleepSched(sleeping);
    }

    public Observable<List<Breed>> getBreeds() {
        return dogRepository.getBreeds();
    }
}
