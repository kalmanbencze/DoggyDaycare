package me.kalmanbncz.doggydaycare;

import android.app.Application;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogDatabase;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class DatabaseModule extends Module {

    public DatabaseModule(Application app) {
        //todo uncomment for production
        bind(DogDatabase.class).toInstance(DogDatabase.getAppDatabase(app));
        ////bind(DogsRetrofitApi.class).toInstance(new MockDogsRetrofitApi());
        ////todo uncomment for production
        ////bind(UserRetrofitApi.class).toInstance(retrofit.create(UserRetrofitApi.class));
        //bind(UserRetrofitApi.class).toInstance(new MockUserRetrofitApi());
        //bind(DogRepository.class).to(DogRepositoryImpl.class);
        //bind(UserRepository.class).to(UserRepositoryImpl.class);
        //bind(UserCache.class).to(UserCacheImpl.class);
    }
}
