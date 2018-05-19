package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.di.scopes.screen.DogsScreenScope;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;
import me.kalmanbncz.doggydaycare.presentation.BaseViewModel;
import me.kalmanbncz.doggydaycare.presentation.RecyclerViewViewModel;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DogsScreenScope
public class DogsViewModel extends RecyclerViewViewModel implements BaseViewModel {

    private static final String TAG = DogsViewModel.class.getSimpleName();

    private final UserRepository userRepository;

    private final DogRepository dogRepository;

    private final DogsAdapter adapter;

    private final Context context;

    private final BehaviorSubject<String> title = BehaviorSubject.create();

    private final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private ReplayProcessor<Integer> paginator = ReplayProcessor.create();

    private int pageIndex = 0;

    private final Observable<String> snackbar = BehaviorSubject.create();

    @Inject
    DogsViewModel(BrowseNavigator browseNavigator, Context context, UserRepository userRepository, DogRepository dogRepository,
                  ResourcesProvider resourcesProvider) {
        this.context = context;
        this.userRepository = userRepository;
        this.dogRepository = dogRepository;
        this.adapter = new DogsAdapter(browseNavigator);
        this.title.onNext(resourcesProvider.getDogsScreenTitle());
        loadMoreItems();
    }

    @Override
    public void onAttach() {
        adapter.setItems(new ArrayList<>());
        subscriptions.clear();
        subscriptions.add(
            paginator.subscribeOn(Schedulers.computation())
                .map(page -> {
                    loading.onNext(true);
                    List<Dog> first = dogRepository.getDogs(page).subscribeOn(Schedulers.computation()).observeOn
                        (AndroidSchedulers.mainThread()).blockingFirst();
                    return first;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    adapter.addItems(items);
                    loading.onNext(false);
                })
        );
    }

    @Override
    public void onDetach() {
        subscriptions.clear();
    }

    public Observable<LoginState> getLoginState() {
        return userRepository.loadCurrentUser().map(this::userToLoginState);
    }

    private LoginState userToLoginState(User user) {
        return user.isValid() ? LoginState.LOGGED_IN : LoginState.LOGGED_OUT;
    }

    public void logout() {
        userRepository.clearCurrentUser();
    }

    public Observable<String> getTitle() {
        return title;
    }

    @Override
    protected DogsAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected LinearLayoutManager createLayoutManager() {
        return new LinearLayoutManager(context);
    }

    @Override
    public Observable<Boolean> getLoadingState() {
        return loading.doOnNext(value -> Log.d(TAG, "getLoadingState: " + value));
    }

    @Override
    public void loadMoreItems() {
        if (!loading.getValue()) {
            loading.onNext(true);
            pageIndex++;
            Log.d(TAG, "loadMoreItems: requesting page " + pageIndex);
            Schedulers.computation().scheduleDirect(() -> paginator.onNext(pageIndex));
        }
    }

    public Observable<String> getSnackbar() {
        return snackbar;
    }
}
