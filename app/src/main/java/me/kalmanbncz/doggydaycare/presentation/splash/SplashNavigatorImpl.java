package me.kalmanbncz.doggydaycare.presentation.splash;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Stack;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.di.AuthModule;
import me.kalmanbncz.doggydaycare.di.BrowseModule;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.AuthFlowScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.SplashFlowScope;
import me.kalmanbncz.doggydaycare.presentation.Navigator;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseActivity;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@SplashFlowScope
public class SplashNavigatorImpl implements SplashNavigator {

    private static final String TAG = "SplashNavigatorImpl";

    private final Context context;

    private Stack<Scope> scopeStack = new Stack<>();

    private Navigator.Executor executor;

    @Inject
    SplashNavigatorImpl(Context context) {
        Log.d(TAG, "SplashNavigatorImpl: created");
        this.context = context;
    }

    @Override
    public void openAuth() {
        Log.d(TAG, "openLogin: ");
        if (executor != null) {
            Scope splash = Toothpick.openScopes(ApplicationScope.class, AuthFlowScope.class);
            splash.bindScopeAnnotation(AuthFlowScope.class);
            splash.installModules(new AuthModule());
            executor.openFlow(new Intent(context, AuthActivity.class), true);
        } else {
            Log.d(TAG, "openLogin: ");
        }
    }

    @Override
    public void openBrowse() {
        if (executor != null) {
            Scope splash = Toothpick.openScopes(ApplicationScope.class, BrowseFlowScope.class);
            splash.bindScopeAnnotation(BrowseFlowScope.class);
            splash.installModules(new BrowseModule());
            executor.openFlow(new Intent(context, BrowseActivity.class), true);
        }
    }

    @Override
    public void openSplash() {
        if (executor != null) {
            Scope scope = Toothpick.openScopes(ApplicationScope.class, SplashFlowScope.class);
            scope.bindScopeAnnotation(SplashFlowScope.class);
            SplashFragment fragment = new SplashFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
        }
    }

    @Override
    public void setExecutor(Navigator.Executor executor) {
        this.executor = executor;
    }

    @Override
    public void removeExecutor() {
        this.executor = null;
    }

    @Override
    public void back() {
        if (executor != null) {
            this.executor.navigateBack();
            if (!scopeStack.empty()) {
                Toothpick.closeScope(scopeStack.pop());
            }
        }
    }
}
