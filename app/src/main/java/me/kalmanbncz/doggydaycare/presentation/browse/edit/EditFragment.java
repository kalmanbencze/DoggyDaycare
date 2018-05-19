package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.EditText;
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
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
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

    @BindView(R.id.name_edittext)
    EditText nameEditText;

    @BindView(R.id.breed_spinner)
    AppCompatSpinner breedSpinner;

    @BindView(R.id.birthdate_spinner)
    AppCompatSpinner birthdateSpinner;

    @BindView(R.id.size_spinner)
    AppCompatSpinner sizeSpinner;

    @BindView(R.id.vaccinations_checkbox)
    AppCompatCheckBox vaccinationsCheckbox;

    @BindView(R.id.neutered_checkbox)
    AppCompatCheckBox neuteredCheckbox;

    @BindView(R.id.friendly_checkbox)
    AppCompatCheckBox friendlyCheckbox;

    @BindView(R.id.gender_spinner)
    AppCompatSpinner genderSpinner;

    @BindView(R.id.commands_edittext)
    EditText commandsEditText;

    @BindView(R.id.eating_edittext)
    EditText eatingEditText;

    @BindView(R.id.walking_edittext)
    EditText walkingEditText;

    @BindView(R.id.sleeping_edittext)
    EditText sleepingEditText;

    @Inject
    BrowseNavigator navigator;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

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

        subscriptions.add(viewModel.getDogBreedsHolder()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::setDogInfoAndBreeds,
                                  this::onError));

        subscriptions.add(viewModel.getTitle()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::setTitle,
                                  this::onError));

        subscriptions.add(viewModel.getLoading()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::onLoading,
                                  this::onError));

        subscriptions.add(viewModel.getSnackbar()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::showSnackbar,
                                  throwable -> Log.e(TAG, getString(R.string.error_message), throwable)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        super.onStop();
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

    private void setDogInfoAndBreeds(DogAndBreedsHolder dogAndBreedsHolder) {
        Dog dog = dogAndBreedsHolder.getDog();
        List<Breed> breeds = dogAndBreedsHolder.getBreeds();
        List<String> converted = new ArrayList<>();
        for (Breed breed : breeds) {
            converted.add(breed.getName());
        }
        nameEditText.setText(dog.getName());
        setAdapterForSpinner(breedSpinner, converted, dog.getBreed(), getString(R.string.breed_hint_label));
    }

    public void setAdapterForSpinner(AppCompatSpinner spinner, List<String> items, String selected, String hint) {
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
        int indexOfSelection = items.indexOf(selected);
        if (selected != null && indexOfSelection >= 0) {
            spinner.setSelection(indexOfSelection);
        } else {
            spinner.setSelection(adapter.getCount()); //display hint
        }
    }

    private void onLoading(Boolean visibility) {
        if (visibility != null) {
            progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
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

    private void onSaved() {
        navigator.back();
    }
}
