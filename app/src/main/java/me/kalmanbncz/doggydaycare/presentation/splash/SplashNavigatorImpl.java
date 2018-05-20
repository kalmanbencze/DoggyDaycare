package me.kalmanbncz.doggydaycare.presentation.splash;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Stack;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.AppScope;
import me.kalmanbncz.doggydaycare.presentation.Navigator;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthActivity;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthFlowScope;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthModule;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseModule;
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
        this.context = context;
    }

    @Override
    public void openAuth() {
        Log.d(TAG, "openLogin: ");
        if (executor != null) {
            while (!scopeStack.empty()) {
                Scope scope = scopeStack.pop();
                Toothpick.closeScope(scope.getName());
                Log.d(TAG, "closeScope: closing " + scope.getName());
            }
            Log.d(TAG, "closeScope: closing SplashFlowScope");
            Toothpick.closeScope(SplashFlowScope.class);
            Scope splash = Toothpick.openScopes(AppScope.class, AuthFlowScope.class);
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
            while (!scopeStack.empty()) {
                Scope scope = scopeStack.pop();
                Toothpick.closeScope(scope.getName());
                Log.d(TAG, "closeScope: closing " + scope.getName());
            }
            Log.d(TAG, "closeScope: closing SplashFlowScope");
            Toothpick.closeScope(SplashFlowScope.class);
            Scope splash = Toothpick.openScopes(AppScope.class, BrowseFlowScope.class);
            splash.bindScopeAnnotation(BrowseFlowScope.class);
            splash.installModules(new BrowseModule());
            executor.openFlow(new Intent(context, BrowseActivity.class), true);
        }
    }

    @Override
    public void openSplash() {
        if (executor != null) {
            Scope scope = Toothpick.openScopes(AppScope.class, SplashFlowScope.class);
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
            if (scopeStack.empty()) {
                Log.d(TAG, "closeScope: closing SplashFlowScope");
                Toothpick.closeScope(SplashFlowScope.class);
            }
        }
    }
}
