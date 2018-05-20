package me.kalmanbncz.doggydaycare.presentation.auth.register;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthNavigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class RegisterFragment extends BaseFragment {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    private final CompositeDisposable subscriptions = new CompositeDisposable();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.text_input_edit_text_username)
    TextInputEditText usernameEditText;

    @BindView(R.id.text_input_edit_text_password)
    TextInputEditText passwordEditText;

    @Inject
    AuthNavigator navigator;

    @Inject
    RegisterViewModel viewModel;

    @BindView(R.id.button_login)
    Button loginButton;

    private void onCompleted() {
        Log.d(TAG, "onCompleted: ");
    }

    private void showFormatError(boolean valid) {
        loginButton.setEnabled(valid);
    }

    private void onStateChanged(LoginState loginState) {
        switch (loginState) {
            case LOGGED_IN:
                navigator.openBrowse();
                break;
            case LOGGED_OUT:
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        subscriptions.clear();
        subscriptions.add(viewModel.getLoginState()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::onStateChanged,
                                  this::onError));
        subscriptions.add(viewModel.getTitle()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::setTitle,
                                  this::onError));
        usernameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getUsernameSubject().onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.getPasswordSubject().onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        subscriptions.add(viewModel.validator()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(valid -> {
                                  if (valid != null) {
                                      showFormatError(valid);
                                  }
                              }));
        usernameEditText.setText("user");
        passwordEditText.setText("123456");
        loginButton.setOnClickListener(v -> {
            subscriptions.add(viewModel.logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                                  .subscribeOn(Schedulers.io())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(
                                      this::onCompleted,
                                      this::onError));
        });
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        super.onStop();
    }
}
