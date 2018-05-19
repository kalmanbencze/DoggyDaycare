package me.kalmanbncz.doggydaycare.data;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class Dog {

    private int id = -1;

    private String name;

    private String breed;

    private String gender;

    private String size;

    private String yearOfBirth;

    private boolean vaccinated;

    private boolean neutered;

    private boolean friendly;

    private String commands;

    private String eatingSched;

    private String sleepSched;

    private String walkSched;

    public Dog(int id, String name, String breed, String gender, String size, String yearOfBirth, boolean vaccinated, boolean neutered,
               boolean friendly, String commands, String eatingSched, String sleepSched, String walkSched) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.gender = gender;
        this.size = size;
        this.yearOfBirth = yearOfBirth;
        this.vaccinated = vaccinated;
        this.neutered = neutered;
        this.friendly = friendly;
        this.commands = commands;
        this.eatingSched = eatingSched;
        this.sleepSched = sleepSched;
        this.walkSched = walkSched;
    }

    public Dog() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public boolean isNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getEatingSched() {
        return eatingSched;
    }

    public void setEatingSched(String eatingSched) {
        this.eatingSched = eatingSched;
    }

    public String getSleepSched() {
        return sleepSched;
    }

    public void setSleepSched(String sleepSched) {
        this.sleepSched = sleepSched;
    }

    public String getWalkSched() {
        return walkSched;
    }

    public void setWalkSched(String walkSched) {
        this.walkSched = walkSched;
    }

    enum DogSize {
        SMALL(0),
        MEDIUM(1),
        LARGE(2),
        GIANT(3);

        private final int code;

        DogSize(int code) {
            this.code = code;
        }
    }

    enum Gender {
        MALE(0),
        FEMALE(1);

        private final int code;

        Gender(int code) {
            this.code = code;
        }
    }

    public static class DogSizeConverter {

        @TypeConverter
        public DogSize toSize(int size) {
            if (size == DogSize.SMALL.code) {
                return DogSize.SMALL;
            } else if (size == DogSize.MEDIUM.code) {
                return DogSize.MEDIUM;
            } else if (size == DogSize.LARGE.code) {
                return DogSize.LARGE;
            } else if (size == DogSize.GIANT.code) {
                return DogSize.GIANT;
            } else {
                throw new IllegalArgumentException("Could not recognize code");
            }
        }

        @TypeConverter
        public int toInteger(DogSize size) {
            return size.code;
        }
    }

    public static class GenderConverter {

        @TypeConverter
        public Gender toGender(int size) {
            if (size == Gender.MALE.code) {
                return Gender.MALE;
            } else if (size == Gender.FEMALE.code) {
                return Gender.FEMALE;
            } else {
                throw new IllegalArgumentException("Could not recognize gender");
            }
        }

        @TypeConverter
        public int toInteger(Gender gender) {
            return gender.code;
        }
    }

}
