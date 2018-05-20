package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.domain.dog.OperationStatus;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;
import org.apache.commons.lang3.EnumUtils;

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
    TextInputEditText commandsEditText;

    @BindView(R.id.commands_textinput)
    TextInputLayout commandsTextInputLayout;

    @BindView(R.id.eating_edittext)
    TextInputEditText eatingEditText;

    @BindView(R.id.eating_textinput)
    TextInputLayout eatingTextInputLayout;

    @BindView(R.id.walking_edittext)
    TextInputEditText walkingEditText;

    @BindView(R.id.walking_textinput)
    TextInputLayout walkingTextInputLayout;

    @BindView(R.id.sleeping_edittext)
    TextInputEditText sleepingEditText;

    @BindView(R.id.sleeping_textinput)
    TextInputLayout sleepingTextInputLayout;

    @Inject
    BrowseNavigator navigator;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private TextChangedListener nameTextWatcher;

    private TextChangedListener commandsTextWatcher;

    private TextChangedListener eatingTextWatcher;

    private TextChangedListener walkingTextWatcher;

    private TextChangedListener sleepingTextWatcher;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }
        setHasOptionsMenu(true);
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
                if (viewModel.getDog().isValid()) {
                    subscriptions.add(viewModel.save()
                                          .subscribeOn(Schedulers.io())
                                          .observeOn(AndroidSchedulers.mainThread())
                                          .subscribe(this::onSaved, this::onError));
                } else {
                    displayErrors();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayErrors() {
        Dog dog = viewModel.getDog();
        if (dog.getName() == null || dog.getName().isEmpty()) {
            nameEditText.setHint(getString(R.string.name_missing_error));
            nameEditText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (dog.getGender() == null) {
            ((TextView) genderSpinner.getSelectedView()).setText(getString(R.string.no_selection_gender_error));
            ((TextView) genderSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (dog.getBreed() == null) {
            ((TextView) breedSpinner.getSelectedView()).setText(getString(R.string.no_selection_breed_error));
            ((TextView) breedSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (dog.getYearOfBirth() == null) {
            ((TextView) birthdateSpinner.getSelectedView()).setText(getString(R.string.no_selection_year_error));
            ((TextView) birthdateSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (dog.getSize() == null) {
            ((TextView) sizeSpinner.getSelectedView()).setText(getString(R.string.no_selection_size_error));
            ((TextView) sizeSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void setDogInfoAndBreeds(DogAndBreedsHolder dogAndBreedsHolder) {
        Dog dog = dogAndBreedsHolder.getDog();
        nameEditText.setText(dog.getName());

        List<Breed> breeds = dogAndBreedsHolder.getBreeds();
        List<String> converted = new ArrayList<>();
        for (Breed breed : breeds) {
            converted.add(breed.getName());
        }
        setAdapterForSpinner(breedSpinner, converted, dog.getBreed(), getString(R.string.breed_hint_label));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int i = currentYear; i > currentYear - 20; i--) {
            years.add(String.valueOf(i));
        }
        setAdapterForSpinner(birthdateSpinner, years, dog.getYearOfBirth(), getString(R.string.birth_year_hint_label));

        List<Dog.DogSize> enumlist = EnumUtils.getEnumList(Dog.DogSize.class);
        List<String> sizes = new ArrayList<>();
        for (Dog.DogSize size : enumlist) {
            sizes.add(size.code);
        }
        setAdapterForSpinner(sizeSpinner, sizes, dog.getSize(), getString(R.string.size_hint_label));

        vaccinationsCheckbox.setChecked(dog.isVaccinated());
        neuteredCheckbox.setChecked(dog.isNeutered());
        friendlyCheckbox.setChecked(dog.isFriendly());

        List<Dog.Gender> genderList = EnumUtils.getEnumList(Dog.Gender.class);
        List<String> genders = new ArrayList<>();
        for (Dog.Gender size : genderList) {
            genders.add(size.code);
        }
        setAdapterForSpinner(genderSpinner, genders, dog.getGender(), getString(R.string.gender_hint_label));

        commandsEditText.setText(dog.getCommands());
        eatingEditText.setText(dog.getEatingSched());
        walkingEditText.setText(dog.getWalkSched());
        sleepingEditText.setText(dog.getSleepSched());
    }

    public void setAdapterForSpinner(AppCompatSpinner spinner, List<String> items, String selected, String hint) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public int getCount() {
                return super.getCount() - 1; // you dont display last item. It is used as hint.
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {

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
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        setListeners();
        subscriptions.add(viewModel.getDogAndBreedsHolder()
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
    public void onStop() {
        removeListeners();
        subscriptions.clear();
        super.onStop();
    }

    public void setListeners() {
        nameTextWatcher = new TextChangedListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getDog().setName(editable.toString());
            }
        };
        nameEditText.addTextChangedListener(nameTextWatcher);
        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getAdapter().getItem(i);
                if (value != null && !value.contains("*")) {
                    viewModel.getDog().setBreed(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                viewModel.getDog().setBreed(null);
            }
        });
        birthdateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getAdapter().getItem(i);
                if (value != null && !value.contains("*")) {
                    viewModel.getDog().setYearOfBirth(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                viewModel.getDog().setYearOfBirth(null);
            }
        });
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getAdapter().getItem(i);
                if (value != null && !value.contains("*")) {
                    viewModel.getDog().setSize(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                viewModel.getDog().setSize(null);
            }
        });
        vaccinationsCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                viewModel.getDog().setVaccinated(b);
            }
        });
        neuteredCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                viewModel.getDog().setNeutered(b);
            }
        });
        friendlyCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                viewModel.getDog().setFriendly(b);
            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = (String) adapterView.getAdapter().getItem(i);
                if (value != null && !value.contains("*")) {
                    viewModel.getDog().setGender(value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                viewModel.getDog().setGender(null);
            }
        });
        commandsTextWatcher = new TextChangedListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getDog().setCommands(editable.toString());
            }
        };
        commandsEditText.addTextChangedListener(commandsTextWatcher);
        eatingTextWatcher = new TextChangedListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getDog().setEatingSched(editable.toString());
            }
        };
        eatingEditText.addTextChangedListener(eatingTextWatcher);
        walkingTextWatcher = new TextChangedListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getDog().setWalkSched(editable.toString());
            }
        };
        walkingEditText.addTextChangedListener(walkingTextWatcher);
        sleepingTextWatcher = new TextChangedListener() {

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.getDog().setSleepSched(editable.toString());
            }
        };
        sleepingEditText.addTextChangedListener(sleepingTextWatcher);
    }

    private void removeListeners() {
        nameEditText.removeTextChangedListener(nameTextWatcher);
        nameTextWatcher = null;
        breedSpinner.setOnItemSelectedListener(null);
        birthdateSpinner.setOnItemSelectedListener(null);
        sizeSpinner.setOnItemSelectedListener(null);
        vaccinationsCheckbox.setOnCheckedChangeListener(null);
        neuteredCheckbox.setOnCheckedChangeListener(null);
        friendlyCheckbox.setOnCheckedChangeListener(null);
        genderSpinner.setOnItemSelectedListener(null);
        commandsEditText.removeTextChangedListener(commandsTextWatcher);
        commandsTextWatcher = null;
        eatingEditText.removeTextChangedListener(eatingTextWatcher);
        eatingTextWatcher = null;
        walkingEditText.removeTextChangedListener(walkingTextWatcher);
        walkingTextWatcher = null;
        sleepingEditText.removeTextChangedListener(sleepingTextWatcher);
        sleepingTextWatcher = null;
    }

    private void onSaved(OperationStatus status) {
        if (status.isSuccess()) {
            navigator.back();
        } else {
            showSnackbar(getString(R.string.error_message_sync), Snackbar.LENGTH_INDEFINITE, getString(R.string.ok),
                         () -> navigator.back());
        }
    }

    private class TextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
