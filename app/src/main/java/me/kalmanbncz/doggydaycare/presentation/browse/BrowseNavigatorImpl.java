package me.kalmanbncz.doggydaycare.presentation.browse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Stack;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.di.AuthModule;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.AuthFlowScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.di.scopes.screen.DogsScreenScope;
import me.kalmanbncz.doggydaycare.di.scopes.screen.EditScreenScope;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.dogs.DogsFragment;
import me.kalmanbncz.doggydaycare.presentation.browse.edit.EditFragment;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
//@DebugLog
@BrowseFlowScope
public class BrowseNavigatorImpl implements BrowseNavigator {

    private static final String TAG = "BrowseNavigatorImpl";

    private final Context context;

    private Stack<Scope> scopeStack = new Stack<>();

    private Executor executor;

    @Inject
    BrowseNavigatorImpl(Context context) {
        Log.d(TAG, "BrowseNavigatorImpl: created");
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
    public void openDogList() {
        if (executor != null) {
            Scope scope = Toothpick.openScopes(ApplicationScope.class, BrowseFlowScope.class, DogsScreenScope.class);
            scope.bindScopeAnnotation(DogsScreenScope.class);
            DogsFragment fragment = new DogsFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
        }
    }

    @Override
    public void openEdit(Dog dog) {
        if (executor != null) {
            Scope scope =
                Toothpick.openScopes(ApplicationScope.class, BrowseFlowScope.class, DogsScreenScope.class, EditScreenScope.class);
            scope.installModules(new Module() {{
                bind(Dog.class).toInstance(dog);
            }});
            scope.bindScopeAnnotation(EditScreenScope.class);
            EditFragment fragment = new EditFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
        }
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

    @Override
    public void openAdd() {
        if (executor != null) {
            Scope scope =
                Toothpick.openScopes(ApplicationScope.class, BrowseFlowScope.class, DogsScreenScope.class, EditScreenScope.class);
            scope.installModules(new Module() {{
                bind(Dog.class).toInstance(new Dog());
            }});
            scope.bindScopeAnnotation(EditScreenScope.class);
            EditFragment fragment = new EditFragment();
            Toothpick.inject(fragment, scope);
            scopeStack.add(scope);
            executor.showScreen(fragment);
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
}
