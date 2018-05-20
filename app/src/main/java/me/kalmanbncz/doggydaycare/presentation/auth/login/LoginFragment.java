package me.kalmanbncz.doggydaycare.presentation.auth.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class LoginFragment extends BaseFragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    @BindView(R.id.text_input_edit_text_username)
    protected TextInputEditText usernameEditText;

    @BindView(R.id.text_input_edit_text_password)
    protected TextInputEditText passwordEditText;

    @BindView(R.id.button_login)
    protected AppCompatButton loginButton;

    @BindView(R.id.button_register)
    protected AppCompatButton registerButton;

    @Inject
    AuthNavigator navigator;

    @Inject
    LoginViewModel viewModel;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    private void onCompleted() {
        Log.d(TAG, "onCompleted: ");
    }

    private void showFormatError(boolean valid) {
        loginButton.setEnabled(valid);
    }

    private void onStateChanged(LoginState loginState) {
        switch (loginState) {
            case LOGGED_IN:
                Log.d("Kali", "onStateChanged: opening browse flow");
                navigator.openBrowse();
                break;
            case LOGGED_OUT:
                loginButton.setEnabled(true);
                usernameEditText.setEnabled(true);
                passwordEditText.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                break;
            case LOGIN_ERROR:
                loginButton.setEnabled(true);
                usernameEditText.setEnabled(true);
                passwordEditText.setEnabled(true);
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

        subscriptions.add(viewModel.getSnackbar()
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(
                                  this::showSnackbar,
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
        if (usernameEditText.getText().length() == 0) {
            usernameEditText.setText("user1");
            passwordEditText.setText("123456");
        }
        loginButton.setOnClickListener(v -> {
            subscriptions.add(viewModel.logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                                  .subscribeOn(Schedulers.io())
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(
                                      this::onCompleted,
                                      this::onError));
            v.setEnabled(false);
            usernameEditText.setEnabled(false);
            passwordEditText.setEnabled(false);
        });
        registerButton.setOnClickListener(view -> navigator.openRegister());
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        super.onStop();
    }
}
