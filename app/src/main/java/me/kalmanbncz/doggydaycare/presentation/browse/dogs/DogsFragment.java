package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.Util;
import me.kalmanbncz.doggydaycare.data.Dog;
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

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    protected NavigationView navigationView;

    @BindView(R.id.empty_layout)
    protected ViewGroup emptyLayout;

    @BindView(R.id.add_first_dog)
    protected AppCompatButton addFirstDogButton;

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

    private DogsAdapter adapter;

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
        initializeSideMenuDrawer();
        adapter = new DogsAdapter(browseNavigator);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.setItems(new ArrayList<>());
        subscriptions.clear();
        subscriptions.add(viewModel.getLoginState()
                              .distinctUntilChanged()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::onStateChanged,
                                  this::onError));

        subscriptions.add(viewModel.getUser()
                              .distinctUntilChanged()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::onUserNameChanged,
                                  this::onError));

        subscriptions.add(viewModel.getLoadingState()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::loading,
                                  this::onError));

        subscriptions.add(viewModel.getDogs()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::addDogs,
                                  this::onError));

        subscriptions.add(viewModel.getTitle()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::setTitle,
                                  this::onError));

        subscriptions.add(viewModel.getSnackbar()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::showSnackbar,
                                  throwable -> Log.e(TAG, "Something went wrong, please try again later.", throwable)));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        itemDecoration = new VerticalSpaceItemDecoration((int) Util.dpToPx(getContext(), 1));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onStop() {
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.removeItemDecoration(itemDecoration);
        itemDecoration = null;
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);
        subscriptions.clear();
        super.onStop();
    }

    private void onUserNameChanged(String username) {
        View headerView = navigationView.getHeaderView(0);

        TextView usernameTextView = headerView.findViewById(R.id.user_textview);
        usernameTextView.setText(username);
    }

    private void addDogs(List<Dog> dogs) {
        adapter.setItems(dogs);
        if (dogs.size() > 0) {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            addFirstDogButton.setOnClickListener(null);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            addFirstDogButton.setOnClickListener(view -> browseNavigator.openAdd());
        }
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

    private void initializeSideMenuDrawer() {


        /* Setting Navigation View Item Selected Listener to handle the item click of the
         navigation menu. This method will trigger on item Click of navigation menu
         */
        navigationView.setNavigationItemSelectedListener(item -> {
            // Closing drawer on item click
            drawerLayout.closeDrawers();
            switch (item.getItemId()) {
                case R.id.navigation_about:
                    showSnackbar("About Selected");
                    return true;
                case R.id.navigation_logout:
                    new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout_title_label)
                        .setMessage(R.string.log_out_confirmation_message)
                        .setPositiveButton(R.string.yes_label, (dialogInterface, i) -> viewModel.logout())
                        .setNegativeButton(R.string.no_label, (dialogInterface, i) -> dialogInterface.cancel())
                        .show();
                    return true;
            }
            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                                                                                drawerLayout, toolbar, R.string.app_name,
                                                                                R.string.app_name);
        // Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        // Calling sync state is necessary or else your hamburger icon won't show up
        actionBarDrawerToggle.syncState();
    }
}
