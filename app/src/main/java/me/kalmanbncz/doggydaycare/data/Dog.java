package me.kalmanbncz.doggydaycare.data;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class Dog {

    private int id;

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
}
