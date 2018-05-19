package me.kalmanbncz.doggydaycare.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import me.kalmanbncz.doggydaycare.R;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public abstract class BaseFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private Snackbar snackbar;

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public abstract String getScreenTag();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public ActionBar getActionBar() {
        return getActivity() != null ? ((AppCompatActivity) getActivity()).getSupportActionBar() : null;
    }

    public void setTitle(String title) {
        Log.d(TAG, "setTitle: " + title);
        if (getActivity() != null) {
            ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionbar != null) {
                actionbar.setTitle(title);
            }
        }
    }

    protected void onError(Throwable throwable) {
        showSnackBar(throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(final @StringRes int resId, final int duration) {
        showSnackBar(resId, duration, null, null);
    }

    public void showSnackBar(final CharSequence text, final int duration) {
        showSnackBar(text, duration, null, null);
    }

    public void showSnackBar(final @StringRes int resId, final int duration, final String button, final Runnable onClick) {
        showSnackBar(getText(resId), duration, button, onClick);
    }

    public void showSnackBar(final @StringRes int resId, final int duration, final @StringRes int buttonResId, final Runnable onClick) {
        showSnackBar(getText(resId), duration, getText(buttonResId), onClick);
    }

    public void showSnackBar(final CharSequence text, final int duration, final CharSequence button, final Runnable onClick) {
        uiHandler.post(() -> {
            View view = getView();
            if (snackbar != null && snackbar.isShown()) {
                snackbar.setText(text);
                snackbar.setDuration(duration);
                if (button != null && onClick != null) {
                    snackbar.setAction(button, v -> onClick.run());
                }
                return;
            }
            if (view == null) {
                return;
            }
            snackbar = Snackbar.make(view, text, duration);
            if (button != null && onClick != null) {
                snackbar.setAction(button, v -> onClick.run());
            }
            snackbar.show();
        });
    }

    public void showSnackBar(String s) {
        showSnackBar(s, Snackbar.LENGTH_SHORT);
    }
}
