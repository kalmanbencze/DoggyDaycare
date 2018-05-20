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
@Database(entities = {DogEntity.class, BreedEntity.class}, version = 1)
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
                            //INSTANCE.insertDogData();

                            INSTANCE.insertBreedData();
                        });
                    }
                })
                .build();
        }
        return INSTANCE;
    }

    private void insertBreedData() {
        ArrayList<BreedEntity> breeds = new ArrayList<>();
        breeds.add(new BreedEntity(1, "French Bulldog"));
        breeds.add(new BreedEntity(2, "German Shepherd"));
        breeds.add(new BreedEntity(3, "Poodle"));
        breeds.add(new BreedEntity(4, "Chihuahua"));
        breeds.add(new BreedEntity(5, "Golden Retriever"));
        breeds.add(new BreedEntity(6, "Yorkshire Terrier"));
        breeds.add(new BreedEntity(7, "Dachshund (all varieties)"));
        breeds.add(new BreedEntity(8, "Beagle"));
        breeds.add(new BreedEntity(9, "Boxer"));
        breeds.add(new BreedEntity(10, "Miniature Schnauzer"));
        breeds.add(new BreedEntity(11, "Shih Tzu"));
        breeds.add(new BreedEntity(12, "Bulldog"));
        breeds.add(new BreedEntity(13, "German Spitz"));
        breeds.add(new BreedEntity(14, "English Cocker Spaniel"));
        breeds.add(new BreedEntity(15, "Cavalier King Charles Spaniel"));
        breeds.add(new BreedEntity(16, "Labrador Retriever"));
        breeds.add(new BreedEntity(17, "Pug"));
        breeds.add(new BreedEntity(18, "Rottweiler"));
        breeds.add(new BreedEntity(19, "English Setter"));
        breeds.add(new BreedEntity(20, "Maltese"));
        breeds.add(new BreedEntity(21, "English Springer Spaniel"));
        breeds.add(new BreedEntity(22, "German Shorthaired Pointer"));
        breeds.add(new BreedEntity(23, "Staffordshire Bull Terrier"));
        breeds.add(new BreedEntity(24, "Border Collie"));
        breeds.add(new BreedEntity(25, "Shetland Sheepdog"));
        breeds.add(new BreedEntity(26, "Dobermann"));
        breeds.add(new BreedEntity(27, "West Highland White Terrier"));
        breeds.add(new BreedEntity(28, "Bernese Mountain Dog"));
        breeds.add(new BreedEntity(29, "Great Dane"));
        breeds.add(new BreedEntity(30, "Brittany Spaniel"));

        for (BreedEntity entity : breeds) {
            INSTANCE.dogDao().insert(entity);
        }
    }

    private void insertDogData() {
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
    }

    public abstract DogDao dogDao();
}
