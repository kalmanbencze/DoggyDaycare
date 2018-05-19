package me.kalmanbncz.doggydaycare;

import android.util.Log;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class Util {

    private static final String TAG = "Util";

    @SuppressWarnings("unused")
    public static void trace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : trace) {
            Log.d(TAG, "####### " + e);
        }
    }
}
