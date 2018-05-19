package me.kalmanbncz.doggydaycare.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.AuthFlowScope;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.Navigator;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class AuthActivity extends AppCompatActivity implements Navigator.Executor {

    private static final String TAG = AuthActivity.class.getSimpleName();

    @Inject
    AuthNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scope auth = Toothpick.openScopes(ApplicationScope.class, AuthFlowScope.class);
        auth.bindScopeAnnotation(AuthFlowScope.class);
        Toothpick.inject(this, auth);
        setContentView(R.layout.activity_auth);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null && navigator != null) {
            navigator.openLogin();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (navigator != null) {
            navigator.setExecutor(this);
        }
    }

    @Override
    protected void onStop() {
        if (navigator != null) {
            navigator.removeExecutor();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        navigator.back();
    }

    @Override
    public void showScreen(BaseFragment screen) {
        runOnUiThread(() -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(screen.getScreenTag());
            transaction.add(R.id.main_container, screen, screen.getScreenTag());
            transaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        });
    }

    @Override
    public void openFlow(Intent intent, boolean closeCurrent) {
        startActivity(intent);
        if (closeCurrent) {
            finish();
        }
    }

    @Override
    public void navigateBack() {
        runOnUiThread(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().executePendingTransactions();
            } else {
                finish();
            }
        });
    }
}

