package me.kalmanbncz.doggydaycare.di;

import android.app.Application;
import android.content.Context;
import me.kalmanbncz.doggydaycare.di.provider.RetrofitProvider;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.ResourcesProviderImpl;
import retrofit2.Retrofit;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class AppModule extends Module {

    public AppModule(Application app) {
        bind(Retrofit.class).toProvider(RetrofitProvider.class);
        bind(ResourcesProvider.class).to(ResourcesProviderImpl.class);
        bind(Context.class).toInstance(app);
    }
}
