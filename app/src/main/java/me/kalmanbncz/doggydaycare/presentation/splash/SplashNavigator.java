package me.kalmanbncz.doggydaycare.presentation.splash;

import hugo.weaving.DebugLog;
import me.kalmanbncz.doggydaycare.di.scopes.flow.SplashFlowScope;
import me.kalmanbncz.doggydaycare.presentation.Navigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DebugLog
@SplashFlowScope
public interface SplashNavigator extends Navigator {

    void openAuth();

    void openBrowse();

    void openSplash();
}
