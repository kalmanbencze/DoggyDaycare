package me.kalmanbncz.doggydaycare.domain.dog.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by kalman.bencze on 20/05/2018.
 */
@Entity(tableName = "DogsTable")
public class DogEntity {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "ownerId")
    public int ownerId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "breed")
    public String breed;

    @ColumnInfo(name = "yearOfBirth")
    public String yearOfBirth;

    @ColumnInfo(name = "size")
    public String size;

    @ColumnInfo(name = "isVaccinated")
    public boolean isVaccinated;

    @ColumnInfo(name = "isNeutered")
    public boolean isNeutered;

    @ColumnInfo(name = "isDogFriendly")
    public boolean isDogFriendly;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "vaccinated")
    public boolean vaccinated;

    @ColumnInfo(name = "neutered")
    public boolean neutered;

    @ColumnInfo(name = "friendly")
    public boolean friendly;

    @ColumnInfo(name = "commands")
    public String commands;

    @ColumnInfo(name = "eatingSched")
    public String eatingSched;

    @ColumnInfo(name = "sleepSched")
    public String sleepSched;

    @ColumnInfo(name = "walkSched")
    public String walkSched;

    @ColumnInfo(name = "timestamp")
    public long timestamp;
}
