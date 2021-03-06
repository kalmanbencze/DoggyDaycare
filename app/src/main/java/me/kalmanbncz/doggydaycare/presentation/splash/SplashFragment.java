package me.kalmanbncz.doggydaycare.presentation.splash;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class SplashFragment extends BaseFragment {

    private static final String TAG = SplashFragment.class.getSimpleName();

    @Inject
    SplashNavigator navigator;

    @Inject
    SplashViewModel viewModel;

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    private void forward(Boolean aBoolean) {
        if (aBoolean != null && aBoolean) {
            navigator.openBrowse();
        } else {
            navigator.openAuth();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        subscriptions.clear();
        subscriptions.add(viewModel.loggedIn()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::forward));
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        super.onStop();
    }
}
