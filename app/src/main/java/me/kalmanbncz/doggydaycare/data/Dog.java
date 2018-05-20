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

    private String commands = "";

    private String eatingSched = "";

    private String sleepSched = "";

    private String walkSched = "";

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

    public Dog(Dog dog) {
        this.id = dog.id;
        this.name = dog.name;
        this.breed = dog.breed;
        this.gender = dog.gender;
        this.size = dog.size;
        this.yearOfBirth = dog.yearOfBirth;
        this.vaccinated = dog.vaccinated;
        this.neutered = dog.neutered;
        this.friendly = dog.friendly;
        this.commands = dog.commands;
        this.eatingSched = dog.eatingSched;
        this.sleepSched = dog.sleepSched;
        this.walkSched = dog.walkSched;
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

    public boolean isValid() {
        return !(
            name == null ||
                breed == null ||
                yearOfBirth == null ||
                size == null ||
                gender == null ||
                commands == null ||
                eatingSched == null ||
                walkSched == null ||
                sleepSched == null ||
                name.isEmpty() ||
                breed.isEmpty() ||
                yearOfBirth.isEmpty() ||
                size.isEmpty() ||
                gender.isEmpty()
        );
    }

    public enum DogSize {
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large"),
        GIANT("Giant");

        public final String code;

        DogSize(String code) {
            this.code = code;
        }
    }

    public enum Gender {
        MALE("Male"),
        FEMALE("Female");

        public final String code;

        Gender(String code) {
            this.code = code;
        }
    }

    public static class DogSizeConverter {

        @TypeConverter
        public DogSize toSize(String size) {
            if (size.equals(DogSize.SMALL.code)) {
                return DogSize.SMALL;
            } else if (size.equals(DogSize.MEDIUM.code)) {
                return DogSize.MEDIUM;
            } else if (size.equals(DogSize.LARGE.code)) {
                return DogSize.LARGE;
            } else if (size.equals(DogSize.GIANT.code)) {
                return DogSize.GIANT;
            } else {
                throw new IllegalArgumentException("Could not recognize code");
            }
        }

        @TypeConverter
        public String toString(DogSize size) {
            return size.code;
        }
    }

    public static class GenderConverter {

        @TypeConverter
        public Gender toGender(String size) {
            if (size.equals(Gender.MALE.code)) {
                return Gender.MALE;
            } else if (size.equals(Gender.FEMALE.code)) {
                return Gender.FEMALE;
            } else {
                throw new IllegalArgumentException("Could not recognize gender");
            }
        }

        @TypeConverter
        public String toString(Gender gender) {
            return gender.code;
        }
    }
}
