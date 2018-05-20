package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import io.reactivex.Observable;
import java.util.List;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.domain.dog.OperationStatus;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
interface EditViewModel {

    Observable<DogAndBreedsHolder> getDogAndBreedsHolder();

    Observable<OperationStatus> save();

    Observable<String> getTitle();

    Observable<String> getSnackbar();

    Observable<Boolean> getLoading();

    Observable<List<Breed>> getBreeds();
}
