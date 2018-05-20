package me.kalmanbncz.doggydaycare.domain.dog.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by kalman.bencze on 20/05/2018.
 */
@Entity(tableName = "BreedsTable")
public class BreedEntity {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public
    int id;

    @ColumnInfo(name = "name")
    public
    String name;

    @ColumnInfo(name = "timestamp")
    long timestamp;

    public BreedEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}