package me.kalmanbncz.doggydaycare;

import android.app.Application;
import android.content.Context;
import me.kalmanbncz.doggydaycare.domain.ResourcesProvider;
import me.kalmanbncz.doggydaycare.domain.ResourcesProviderImpl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
class AppModule extends Module {

    private static final String BASE_URL = "http://dogs.org/";

    AppModule(Application app) {
        bind(Retrofit.class).toInstance(new Retrofit.Builder()
                                            .baseUrl(BASE_URL)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                            .build());
        bind(ResourcesProvider.class).to(ResourcesProviderImpl.class);
        bind(Context.class).toInstance(app);
    }
}
