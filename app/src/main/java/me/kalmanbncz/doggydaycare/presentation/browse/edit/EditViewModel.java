package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.di.scopes.screen.EditScreenScope;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.presentation.BaseViewModel;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@EditScreenScope
public class EditViewModel implements BaseViewModel {

    private final DogRepository dogRepository;

    public PublishSubject<String> title = PublishSubject.create();

    public PublishSubject<String> snackbar = PublishSubject.create();

    public BehaviorSubject<Boolean> loading = BehaviorSubject.create();

    private Dog dog;

    @Inject
    EditViewModel(DogRepository dogRepository, Dog dog) {
        this.dogRepository = dogRepository;
        this.dog = dog;
        title.onNext("Edit");
        snackbar.onNext("Editing");
        loading.onNext(true);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
