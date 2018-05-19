package me.kalmanbncz.doggydaycare.domain;

import android.support.annotation.StringRes;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@ApplicationScope
public interface ResourcesProvider {

    String getString(@StringRes int id);

    String getApiKey();

    String getDogsScreenTitle();

    String getCreateScreenTitle();

    String getEditScreenTitle();

    String getWelcomeMessage(String user);

    String getLoginErrorMessage();
}
