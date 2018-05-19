package me.kalmanbncz.doggydaycare.presentation.splash;

import android.app.ActionBar;
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

    private CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    public void onStart() {
        super.onStart();
        subscriptions.clear();
        viewModel.onAttach();
        subscriptions.add(viewModel.loggedIn()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(this::forward));
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        viewModel.onDetach();
        super.onStop();
    }

    private void forward(Boolean aBoolean) {
        if (aBoolean != null && aBoolean) {
            navigator.openBrowse();
        } else {
            navigator.openAuth();
        }
    }

    @Override
    public String getScreenTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        return view;
    }

    public void setupActionbar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
    }
}
