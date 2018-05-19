package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class EditFragment extends BaseFragment {

    private static final String TAG = EditFragment.class.getSimpleName();

    @Inject
    EditViewModel viewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    BrowseNavigator navigator;

    private View view;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        subscriptions.add(viewModel.getTitle()
                              .subscribe(
                                  this::setTitle,
                                  this::onError));

        subscriptions.add(viewModel.getLoading()
                              .subscribe(
                                  this::setLoadingIndicatorVisibility,
                                  this::onError));

        subscriptions.add(viewModel.getSnackbar()
                              .subscribe(
                                  this::showSnackbar,
                                  throwable -> Log.e(TAG, "Something went wrong, please try again later.", throwable)));
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        super.onStop();
    }

    public void setAdapterForSpinner(AppCompatSpinner spinner, List<String> items, String hint) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView) v.findViewById(android.R.id.text1)).setText("");
                    ((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(items);
        adapter.add(hint);

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount()); //display hint
    }

    private void setLoadingIndicatorVisibility(Boolean visibility) {
        //if (visibility != null) {
        //progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
        //}
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                subscriptions.add(viewModel.save().subscribe(this::onSaved, this::onError));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSaved() {
        navigator.back();
    }
}
