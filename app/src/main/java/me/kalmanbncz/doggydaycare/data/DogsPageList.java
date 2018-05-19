package me.kalmanbncz.doggydaycare.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DogsPageList {

    private final int page;

    private final int totalPages;

    private final int totalDogsCount;

    private final List<Dog> dogs;

    private final Throwable error;

    public DogsPageList(int page,
                        int totalPages,
                        int totalDogsCount,
                        List<Dog> dogs) {
        this.page = page;
        this.totalPages = totalPages;
        this.totalDogsCount = totalDogsCount;
        this.dogs = dogs;
        this.error = null;
    }

    public DogsPageList(Throwable error) {
        this.page = 0;
        this.totalPages = 0;
        this.totalDogsCount = 0;
        this.dogs = new ArrayList<>();
        this.error = error;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalDogsCount() {
        return totalDogsCount;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public Throwable getError() {
        return error;
    }
}
