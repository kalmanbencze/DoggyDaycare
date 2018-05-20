package me.kalmanbncz.doggydaycare.domain.user.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import javax.inject.Inject;
import me.kalmanbncz.doggydaycare.AppScope;
import me.kalmanbncz.doggydaycare.data.User;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@AppScope
public class UserCacheImpl implements UserCache {

    private static final String KEY_USER_NAME = "userName";

    private static final String KEY_USER_ID = "userId";

    private static final String KEY_USER_TOKEN = "userToken";

    private static final String TAG = "UserCacheImpl";

    private final SharedPreferences preferences;

    private final BehaviorSubject<User> user = BehaviorSubject.create();

    @Inject
    UserCacheImpl(Context context) {
        Log.d(TAG, "UserCacheImpl: created");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        emitUser();
    }

    @Override
    public Observable<User> getCurrentUser() {
        return user;
    }

    @Override
    public void setCurrentUser(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_TOKEN, user.getToken());
        editor.commit();
        emitUser();
    }

    @Override
    public void removeCurrentUser() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_TOKEN);
        editor.commit();
        emitUser();
    }

    private void emitUser() {
        Log.d(TAG, "emitUser: ");
        user.onNext(new User(
            preferences.getInt(KEY_USER_ID, -1),
            preferences.getString(KEY_USER_NAME, null),
            preferences.getString(KEY_USER_TOKEN, null)));
    }
}
