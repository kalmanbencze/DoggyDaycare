package me.kalmanbncz.doggydaycare.domain.dog.persistence;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
@Database(entities = {DogEntity.class, BreedEntity.class}, version = 1, exportSchema = true)
public abstract class DogDatabase extends RoomDatabase {

    private static DogDatabase INSTANCE;

    public static DogDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DogDatabase.class, "DogDatabase")
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            ArrayList<DogEntity> results = new ArrayList<>();
                            for (int i = 0; i < 80; i++) {
                                DogEntity result = new DogEntity();
                                result.id = i;
                                result.name = "localdog" + (i + 1);
                                result.breed = "breed" + (i + 1);
                                result.gender = i % 2 == 0 ? "Male" : "Female";
                                result.neutered = new Random().nextBoolean();
                                result.friendly = new Random().nextBoolean();
                                result.vaccinated = new Random().nextBoolean();
                                result.size = i % 2 == 0 ? "Small" : "Large";
                                result.yearOfBirth = i % 2 == 0 ? "2014" : "2016";
                                result.commands = i % 2 == 0 ? "Sit, stay." : "sit, stay, jump, low, roll.";
                                result.walkSched = i % 2 == 0 ? "Daily in the evening." : "Every two days.";
                                result.eatingSched = i % 2 == 0 ? "2 times a day, morning and afternoon." : "One daily, around 2:pm";
                                result.sleepSched = i % 2 == 0 ? "All day." : "Naps around 12pm, and in the afternoon.";
                                results.add(result);
                            }

                            INSTANCE.dogDao().insertAll(results.toArray(new DogEntity[0]));
                        });
                    }
                })
                .build();
        }
        return INSTANCE;
    }

    public abstract DogDao dogDao();
}
