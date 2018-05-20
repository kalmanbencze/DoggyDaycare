package me.kalmanbncz.doggydaycare.presentation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.kalmanbncz.doggydaycare.R;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public abstract class BaseFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {

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
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getFragmentManager() != null) {
            getFragmentManager().addOnBackStackChangedListener(this);
        }
    }

    @Override
    public void onStop() {
        if (getFragmentManager() != null) {
            getFragmentManager().removeOnBackStackChangedListener(this);
        }
        super.onStop();
    }

    public ActionBar getActionBar() {
        return getActivity() != null ? ((AppCompatActivity) getActivity()).getSupportActionBar() : null;
    }

    public void setTitle(String title) {
        if (getActivity() != null) {
            ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionbar != null) {
                actionbar.setTitle(title);
            }
        }
    }

    protected void onError(Throwable throwable) {
        showSnackbar(throwable.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
    }

    public void showSnackbar(final @StringRes int resId, final int duration) {
        showSnackbar(resId, duration, null, null);
    }

    public void showSnackbar(final CharSequence text, final int duration) {
        showSnackbar(text, duration, null, null);
    }

    public void showSnackbar(final @StringRes int resId, final int duration, final String button, final Runnable onClick) {
        showSnackbar(getText(resId), duration, button, onClick);
    }

    public void showSnackbar(final @StringRes int resId, final int duration, final @StringRes int buttonResId, final Runnable onClick) {
        showSnackbar(getText(resId), duration, getText(buttonResId), onClick);
    }

    public void showSnackbar(final CharSequence text, final int duration, final CharSequence button, final Runnable onClick) {
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

    public void showSnackbar(String s) {
        showSnackbar(s, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void onBackStackChanged() {
        boolean navigationStackIsEmpty;
        if (getFragmentManager() != null) {
            navigationStackIsEmpty = getFragmentManager().getBackStackEntryCount() <= 1;
            if (getActionBar() != null) {
                Drawable upArrow = AppCompatResources.getDrawable(getContext(), R.drawable.vector_close); ;
                getActionBar().setDisplayHomeAsUpEnabled(!navigationStackIsEmpty);
                if (!navigationStackIsEmpty) {
                    getActionBar().setHomeAsUpIndicator(upArrow);
                }
            }
        }
    }
}
