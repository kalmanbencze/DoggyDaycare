package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import java.util.List;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.LoginState;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
interface DogsViewModel {

    Flowable<List<Dog>> getDogs();

    Observable<LoginState> getLoginState();

    void logout();

    Observable<String> getTitle();

    Observable<Boolean> getLoadingState();

    void loadMoreItems();

    Observable<String> getSnackbar();

    Observable<String> getUser();
}
