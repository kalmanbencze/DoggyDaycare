package me.kalmanbncz.doggydaycare.data;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class Breed {

    private final int id;

    private final String name;

    public Breed(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
