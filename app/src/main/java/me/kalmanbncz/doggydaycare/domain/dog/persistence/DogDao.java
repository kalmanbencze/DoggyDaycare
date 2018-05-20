package me.kalmanbncz.doggydaycare.domain.dog.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import io.reactivex.Flowable;
import java.util.List;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
@Dao
public abstract class DogDao {

    @Query("SELECT * FROM DogsTable")
    public abstract Flowable<List<DogEntity>> getDogs();

    @Query("SELECT * FROM DogsTable")
    public abstract List<DogEntity> getDogsList();

    @Query("SELECT * FROM BreedsTable")
    public abstract Flowable<List<BreedEntity>> getBreeds();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long addOrUpdateDog(DogEntity dogEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(DogEntity... dogEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(BreedEntity... breedEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(BreedEntity entity);
}
