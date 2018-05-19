package me.kalmanbncz.doggydaycare.domain;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.R;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@ApplicationScope
public class ResourcesProviderImpl implements ResourcesProvider {

    private static final String TAG = "ResourcesProvider";

    private final Context context;

    @Inject
    ResourcesProviderImpl(Context context) {
        this.context = context;
        Log.d(TAG, "ResourcesProviderImpl: created");
    }

    @Override
    public String getString(@StringRes int id) {
        return context.getString(id);
    }

    public String getApiKey() {
        return context.getString(R.string.server_api_key);
    }
}
