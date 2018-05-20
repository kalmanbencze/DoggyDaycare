package me.kalmanbncz.doggydaycare.domain.dog;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import java.util.List;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseFlowScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@BrowseFlowScope
public interface DogRepository {

    Flowable<List<Dog>> getDogs();

    Observable<List<Breed>> getBreeds();

    Observable<OperationStatus> addOrUpdate(Dog dog);

    void loadMoreDogs();
}
