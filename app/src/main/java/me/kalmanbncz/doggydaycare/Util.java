package me.kalmanbncz.doggydaycare;

import android.content.Context;
import android.util.DisplayMetrics;
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

    /**
     * Converts the specified DP to PIXELS according to current screen density
     *
     * @param context a
     * @param dp a
     *
     * @return a
     */
    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) Math.max(1, (dp * displayMetrics.density));
    }
}
