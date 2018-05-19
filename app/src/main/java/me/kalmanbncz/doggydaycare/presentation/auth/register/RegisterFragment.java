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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.data.LoginState;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthNavigator;
import me.kalmanbncz.doggydaycare.presentation.auth.login.LoginViewModel;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class RegisterFragment extends BaseFragment {

    private static final String TAG = RegisterFragment.class.getSimpleName();

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.progress_bar)
    protected ProgressBar progressBar;

    @BindView(R.id.text_input_edit_text_username)
    protected TextInputEditText usernameEditText;

    @BindView(R.id.text_input_edit_text_password)
    protected TextInputEditText passwordEditText;

    @BindView(R.id.button_login)
    protected Button loginButton;

    @Inject
    AuthNavigator navigator;

    @Inject
    LoginViewModel viewModel;

    private CompositeDisposable subscriptions = new CompositeDisposable();

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        subscriptions.clear();
        viewModel.onAttach();
        subscriptions.add(viewModel.getLoginState().subscribe(this::onStateChanged, this::onError));
        subscriptions.add(viewModel.getTitle().subscribe(this::setTitle, this::onError));
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
        subscriptions.add(viewModel.validator().subscribe(valid -> {
            if (valid != null) {
                showFormatError(valid);
            }
        }));
        usernameEditText.setText("user");
        passwordEditText.setText("123456");
        loginButton.setOnClickListener(v -> viewModel.logIn(usernameEditText.getText().toString(), passwordEditText.getText().toString()));
    }

    @Override
    public void onStop() {
        subscriptions.clear();
        viewModel.onDetach();
        super.onStop();
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

    @Override
    public String getScreenTag() {
        return TAG;
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
}
