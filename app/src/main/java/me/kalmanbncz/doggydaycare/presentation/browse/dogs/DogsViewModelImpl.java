package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.util.Log;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DogsScreenScope
class DogsViewModelImpl implements DogsViewModel {

    private static final String TAG = DogsViewModelImpl.class.getSimpleName();

    private final UserRepository userRepository;

    private final DogRepository dogRepository;

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);

    private final Observable<String> snackbar = BehaviorSubject.create();

    @Inject
    DogsViewModelImpl(UserRepository userRepository, DogRepository dogRepository,
                      ResourcesProvider resourcesProvider) {
        this.userRepository = userRepository;
        this.dogRepository = dogRepository;
        this.title.onNext(resourcesProvider.getDogsScreenTitle());
        loadMoreItems();
    }

    //public Flowable<List<Dog>> getDogs() {
    //    return paginator.subscribeOn(Schedulers.computation())
    //        .map(page -> {
    //            loading.onNext(true);
    //            List<Dog> first = dogRepository.getDogs(page)
    //                .doOnNext(dogs -> loading.onNext(false))
    //                .subscribeOn(Schedulers.computation())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .blockingFirst();
    //            return first;
    //        })
    //        .observeOn(AndroidSchedulers.mainThread());
    //}

    @Override
    public Flowable<List<Dog>> getDogs() {
        return dogRepository.getDogs().doOnEach(dogs -> {
            Log.d(TAG, "getDogs: " + dogs.toString());
        });
    }

    @Override
    public Observable<LoginState> getLoginState() {
        return userRepository.loadCurrentUser().map(this::userToLoginState);
    }

    @Override
    public void logout() {
        userRepository.clearCurrentUser();
    }

    @Override
    public Observable<String> getTitle() {
        return title;
    }

    @Override
    public Observable<Boolean> getLoadingState() {
        return loading.doOnNext(value -> Log.d(TAG, "getLoadingState: " + value));
    }

    @Override
    public void loadMoreItems() {
        Log.d(TAG, "loadMoreItems: ");
        if (!loading.getValue()) {
            Schedulers.computation().scheduleDirect(dogRepository::loadMoreDogs);
        }
    }

    @Override
    public Observable<String> getSnackbar() {
        return snackbar;
    }

    @Override
    public Observable<String> getUser() {
        return userRepository.loadCurrentUser().map(User::getUsername);
    }

    private LoginState userToLoginState(User user) {
        return user.isValid() ? LoginState.LOGGED_IN : LoginState.LOGGED_OUT;
    }
}
