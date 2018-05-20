package me.kalmanbncz.doggydaycare.presentation.auth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Stack;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.AppScope;
import me.kalmanbncz.doggydaycare.presentation.auth.login.LoginFragment;
import me.kalmanbncz.doggydaycare.presentation.auth.login.LoginModule;
import me.kalmanbncz.doggydaycare.presentation.auth.login.LoginScreenScope;
import me.kalmanbncz.doggydaycare.presentation.auth.register.RegisterFragment;
import me.kalmanbncz.doggydaycare.presentation.auth.register.RegisterModule;
import me.kalmanbncz.doggydaycare.presentation.auth.register.RegisterScreenScope;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseModule;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@AuthFlowScope
class AuthNavigatorImpl implements AuthNavigator {

    private static final String TAG = "AuthNavigatorImpl";

    private final Context context;

    private Stack<Scope> scopeStack = new Stack<>();

    private Executor executor;

    @Inject
    AuthNavigatorImpl(Context context) {
        Log.d(TAG, "AuthNavigatorImpl: created");
        this.context = context;
    }

    @Override
    public void openLogin() {
        Log.d(TAG, "openLogin: ");
        if (executor != null) {
            Scope scope = Toothpick.openScopes(AppScope.class, AuthFlowScope.class, LoginScreenScope.class);
            scope.bindScopeAnnotation(LoginScreenScope.class);
            scope.installModules(new LoginModule());
            LoginFragment fragment = new LoginFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
        } else {
            Log.d(TAG, "openLogin: no executor");
        }
    }

    @Override
    public void openRegister() {
        Log.d(TAG, "openRegister: ");
        if (executor != null) {
            Scope scope = Toothpick.openScopes(AppScope.class, AuthFlowScope.class, RegisterScreenScope.class);
            scope.bindScopeAnnotation(RegisterScreenScope.class);
            scope.installModules(new RegisterModule());
            RegisterFragment fragment = new RegisterFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
        } else {
            Log.d(TAG, "openRegister: no executor");
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
            Log.d(TAG, "closeScope: closing AuthFlowScope");
            Toothpick.closeScope(AuthFlowScope.class);
            Scope browse = Toothpick.openScopes(AppScope.class, BrowseFlowScope.class);
            browse.bindScopeAnnotation(BrowseFlowScope.class);
            browse.installModules(new BrowseModule());
            executor.openFlow(new Intent(context, BrowseActivity.class), true);
        }
    }

    @Override
    public void setExecutor(Executor executor) {
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
                Toothpick.closeScope(scopeStack.pop().getName());
            }
            if (scopeStack.empty()) {
                Log.d(TAG, "closeScope: closing AuthFlowScope");
                Toothpick.closeScope(AuthFlowScope.class);
            }
        }
    }
}
