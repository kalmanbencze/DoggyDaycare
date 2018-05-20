/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.kalmanbncz.doggydaycare;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthActivity;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthFlowScope;
import me.kalmanbncz.doggydaycare.presentation.auth.AuthModule;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.Retrofit;
import toothpick.Scope;
import toothpick.Toothpick;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/**
 * Test class testing the AuthActivity
 * Created by kalman.bencze on 5/20/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AuthActivityTest {

    @Rule
    public IntentsTestRule<AuthActivity> mActivityRule = new IntentsTestRule<AuthActivity>(
        AuthActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            Context appContext = getTargetContext().getApplicationContext();
            Application app = (Application) appContext.getApplicationContext();
            Scope scope = Toothpick.openScopes(AppScope.class);
            scope.bindScopeAnnotation(AppScope.class);
            scope.installModules(new AppModule(app));
            scope.installModules(new BackendApiModule(scope.getInstance(Retrofit.class)));
            scope.getInstance(UserRepository.class).clearCurrentUser();
            scope.installModules(new DatabaseModule(app));
            Toothpick.inject(this, scope);

            Scope splash = Toothpick.openScopes(AppScope.class, AuthFlowScope.class);
            splash.bindScopeAnnotation(AuthFlowScope.class);
            splash.installModules(new AuthModule());
        }

        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            Toothpick.closeScope(AuthFlowScope.class);
            Toothpick.closeScope(AppScope.class);
        }
    };

    @Test
    public void checkLoginButton() {
        onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        onView(withId(R.id.button_login)).check(matches(withText(R.string.login)));
        onView(withId(R.id.text_input_edit_text_username)).check(matches(withText("user1")));
        onView(withId(R.id.text_input_edit_text_username)).perform(replaceText(""));
        onView(withId(R.id.button_login)).check(matches(not(isEnabled())));
        onView(withId(R.id.text_input_edit_text_username)).perform(replaceText("new_user"));
        onView(withId(R.id.text_input_edit_text_password)).perform(replaceText("pass"));//too short
        onView(withId(R.id.button_login)).check(matches(not(isEnabled())));//we check if the button is enabled
    }

    @Test
    public void checkLoginAction() {
        onView(withId(R.id.text_input_edit_text_username)).perform(replaceText("new_user"));
        onView(withId(R.id.text_input_edit_text_password)).perform(replaceText("valid_pass"));//too short
        onView(withId(R.id.button_login)).check(matches(isEnabled()));//we check if the button is enabled
        onView(withId(R.id.button_login)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), BrowseActivity.class)));
    }
}