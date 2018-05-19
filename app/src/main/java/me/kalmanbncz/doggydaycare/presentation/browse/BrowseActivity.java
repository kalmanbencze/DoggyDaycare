package me.kalmanbncz.doggydaycare.presentation.browse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.presentation.BaseFragment;
import me.kalmanbncz.doggydaycare.presentation.Navigator;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BrowseActivity extends AppCompatActivity implements Navigator.Executor {

    private static final String TAG = BrowseActivity.class.getSimpleName();

    @Inject
    BrowseNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scope browse = Toothpick.openScopes(ApplicationScope.class, BrowseFlowScope.class);
        browse.bindScopeAnnotation(BrowseFlowScope.class);
        Toothpick.inject(this, browse);
        setContentView(R.layout.activity_browse);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null && navigator != null) {
            navigator.openDogList();
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
