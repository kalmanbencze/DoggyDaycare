package me.kalmanbncz.doggydaycare;

import me.kalmanbncz.doggydaycare.domain.dog.DogRepository;
import me.kalmanbncz.doggydaycare.domain.dog.DogRepositoryImpl;
import me.kalmanbncz.doggydaycare.domain.dog.api.DogsRetrofitApi;
import me.kalmanbncz.doggydaycare.domain.user.UserRepository;
import me.kalmanbncz.doggydaycare.domain.user.UserRepositoryImpl;
import me.kalmanbncz.doggydaycare.domain.user.api.MockUserRetrofitApi;
import me.kalmanbncz.doggydaycare.domain.user.api.UserRetrofitApi;
import me.kalmanbncz.doggydaycare.domain.user.persistance.UserCache;
import me.kalmanbncz.doggydaycare.domain.user.persistance.UserCacheImpl;
import retrofit2.Retrofit;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BackendApiModule extends Module {

    public BackendApiModule(Retrofit retrofit) {
        //todo uncomment for production
        bind(DogsRetrofitApi.class).toInstance(retrofit.create(DogsRetrofitApi.class));
        //bind(DogsRetrofitApi.class).toInstance(new MockDogsRetrofitApi());
        //todo uncomment for production
        //bind(UserRetrofitApi.class).toInstance(retrofit.create(UserRetrofitApi.class));
        bind(UserRetrofitApi.class).toInstance(new MockUserRetrofitApi());
        bind(DogRepository.class).to(DogRepositoryImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(UserCache.class).to(UserCacheImpl.class);
    }
}
