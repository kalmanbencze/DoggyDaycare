package me.kalmanbncz.doggydaycare.presentation;

import android.content.Intent;
import hugo.weaving.DebugLog;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DebugLog
public interface Navigator {

    void setExecutor(Executor executor);

    void removeExecutor();

    void back();

    interface Executor {

        void showScreen(BaseFragment screen);

        void openFlow(Intent intent, boolean closeCurrent);

        void navigateBack();

        void finish();
    }
}
