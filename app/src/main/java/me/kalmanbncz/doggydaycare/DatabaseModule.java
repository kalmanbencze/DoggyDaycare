package me.kalmanbncz.doggydaycare;

import android.app.Application;
import me.kalmanbncz.doggydaycare.domain.dog.persistence.DogDatabase;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
class DatabaseModule extends Module {

    DatabaseModule(Application app) {
        bind(DogDatabase.class).toInstance(DogDatabase.getAppDatabase(app));
    }
}
