package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.data.User;
import me.kalmanbncz.doggydaycare.di.scopes.screen.DogsScreenScope;
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

    private final BehaviorSubject<String> title = BehaviorSubject.createDefault("My Dogs");

    private final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private BehaviorSubject<LoginState> state = BehaviorSubject.create();

    private ReplayProcessor<Integer> paginator = ReplayProcessor.create();

    private int pageIndex = 0;

    @Inject
    DogsViewModel(BrowseNavigator browseNavigator, Context context, UserRepository userRepository, DogRepository dogRepository) {
        this.context = context;
        this.userRepository = userRepository;
        this.dogRepository = dogRepository;
        this.adapter = new DogsAdapter(browseNavigator);
        loadMoreItems();
    }

    @Override
    public void onAttach() {
        subscriptions.clear();
        subscriptions.add(userRepository.loadCurrentUser().subscribe(user -> {
            if (user.isValid()) {
                state.onNext(LoginState.LOGGED_IN);
            } else {
                state.onNext(LoginState.LOGGED_OUT);
            }
        }));
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
    protected RecyclerView.LayoutManager createLayoutManager() {
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
}
