package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class EditFragment extends BaseFragment {

    private static final String TAG = EditFragment.class.getSimpleName();

    @Inject
    EditViewModel viewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CompositeDisposable disposables;

    private View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
        bindViewModel();
    }

    @Override
    public void onPause() {
        unbindViewModel();
        super.onPause();
    }

    private void unbindViewModel() {
        disposables.clear();
    }

    private void bindViewModel() {
        disposables = new CompositeDisposable();

        disposables.add(viewModel.title
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                this::updateTitle,
                                this::onError));

        disposables.add(viewModel.loading
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                this::setLoadingIndicatorVisibility,
                                this::onError));

        disposables.add(viewModel.snackbar
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                this::showSnackbar,
                                throwable -> Log.e(TAG, "Something went wrong, please try again later.", throwable)));
    }

    private void setLoadingIndicatorVisibility(Boolean visibility) {
        //if (visibility != null) {
        //progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
        //}
    }

    private void showSnackbar(String string) {
        Snackbar.make(view, string, Snackbar.LENGTH_SHORT).show();
    }

    private void updateTitle(String title) {
        if (getActivity() != null) {
            ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionbar != null) {
                actionbar.setTitle(title);
            }
        }
    }

    @Override
    public String getScreenTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        return view;
    }
}
