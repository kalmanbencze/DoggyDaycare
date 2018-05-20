package me.kalmanbncz.doggydaycare.presentation.splash;

import hugo.weaving.DebugLog;
import me.kalmanbncz.doggydaycare.presentation.Navigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DebugLog
@SplashFlowScope
interface SplashNavigator extends Navigator {

    void openAuth();

    void openBrowse();

    void openSplash();
}
