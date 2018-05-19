package me.kalmanbncz.doggydaycare.di.provider;

import javax.inject.Inject;
import javax.inject.Provider;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class RetrofitProvider implements Provider<Retrofit> {

    private static final String BASE_URL = "http://dogs.org/";

    @Inject
    public RetrofitProvider() {

    }

    @Override
    public Retrofit get() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }
}
