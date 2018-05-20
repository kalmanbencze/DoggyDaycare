package me.kalmanbncz.doggydaycare;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseActivity;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.Retrofit;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test class testing the BrowseActivity
 * Created by kalman.bencze on 5/20/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseActivityTest {

    @Rule
    public IntentsTestRule<BrowseActivity> mActivityRule = new IntentsTestRule<BrowseActivity>(
        BrowseActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            Context appContext = getTargetContext().getApplicationContext();
            Application app = (Application) appContext.getApplicationContext();
            Scope scope = Toothpick.openScopes(AppScope.class);
            scope.bindScopeAnnotation(AppScope.class);
            scope.installModules(new AppModule(app));
            scope.installModules(new BackendApiModule(scope.getInstance(Retrofit.class)));
            scope.installModules(new DatabaseModule(app));
            scope.getInstance(UserRepository.class).login("user1", "password");
            Toothpick.inject(this, scope);

            Scope splash = Toothpick.openScopes(AppScope.class, BrowseFlowScope.class);
            splash.bindScopeAnnotation(BrowseFlowScope.class);
            splash.installModules(new BrowseModule());
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Toothpick.closeScope(BrowseFlowScope.class);
            Toothpick.closeScope(AppScope.class);
        }
    };

    @Test
    public void checkLogoutAction() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.navigation_view))
            .perform(NavigationViewActions.navigateTo(R.id.navigation_logout));
        onView(withText(R.string.logout_title_label)).check(matches(isDisplayed()));
        onView(withText(R.string.yes_label)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), AuthActivity.class)));
    }
}