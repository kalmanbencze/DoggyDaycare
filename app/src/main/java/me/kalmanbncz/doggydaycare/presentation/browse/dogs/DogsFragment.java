package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.Util;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DogsFragment extends BaseFragment {

    private static final String TAG = DogsFragment.class.getSimpleName();

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Inject
    BrowseNavigator browseNavigator;

    @BindView(R.id.dogs_list)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    @Inject
    DogsViewModel viewModel;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            if (pastVisibleItems + visibleItemCount >= totalItemCount - 5) {
                viewModel.loadMoreItems();
            }
        }
    };

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private VerticalSpaceItemDecoration itemDecoration;

    @Override
    public String getScreenTag() {
        return TAG;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dogs, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        subscriptions.clear();
        viewModel.onAttach();
        subscriptions.add(viewModel.getLoginState().distinctUntilChanged().subscribe(this::onStateChanged));
        subscriptions.add(
            viewModel.getLoadingState().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::loading));
        subscriptions.add(viewModel.getTitle().subscribe(this::setTitle, this::onError));
        LinearLayoutManager layoutManager = viewModel.createLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(viewModel.getAdapter());
        itemDecoration = new VerticalSpaceItemDecoration((int) Util.dpToPx(getContext(), 1));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onStop() {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.removeItemDecoration(itemDecoration);
        itemDecoration = null;
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        subscriptions.clear();
        viewModel.onDetach();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_browse, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                viewModel.logout();
                return true;
            case R.id.action_add:
                browseNavigator.openAdd();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loading(Boolean loading) {
        if (loading) {
            progressBar.show();
            recyclerView.removeOnScrollListener(scrollListener);
        } else {
            progressBar.hide();
            recyclerView.addOnScrollListener(scrollListener);
        }
    }

    private void onStateChanged(LoginState loginState) {
        switch (loginState) {
            case LOGGED_IN:
                break;
            case LOGGED_OUT:
                browseNavigator.openAuth();
                break;
        }
    }
}
