package me.kalmanbncz.doggydaycare.domain.dog;

import io.reactivex.Observable;
import java.util.List;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@BrowseFlowScope
public interface DogRepository {

    Observable<List<Dog>> getDogs(int page);

    Observable<List<Breed>> getBreeds(int page);
}
