package me.kalmanbncz.doggydaycare.presentation.auth;

import hugo.weaving.DebugLog;
import me.kalmanbncz.doggydaycare.presentation.Navigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DebugLog
@AuthFlowScope
public interface AuthNavigator extends Navigator {

    void openLogin();

    void openRegister();

    void openBrowse();
}
