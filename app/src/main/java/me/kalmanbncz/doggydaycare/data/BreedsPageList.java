package me.kalmanbncz.doggydaycare.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BreedsPageList {

    private final int page;

    private final int totalPages;

    private final int totalDogsCount;

    private final List<Breed> breeds;

    private final Throwable error;

    public BreedsPageList(int page,
                          int totalPages,
                          int totalDogsCount,
                          List<Breed> dogs) {
        this.page = page;
        this.totalPages = totalPages;
        this.totalDogsCount = totalDogsCount;
        this.breeds = dogs;
        this.error = null;
    }

    public BreedsPageList(Throwable error) {
        this.page = 0;
        this.totalPages = 0;
        this.totalDogsCount = 0;
        this.breeds = new ArrayList<>();
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

    public List<Breed> getBreeds() {
        return breeds;
    }

    public Throwable getError() {
        return error;
    }
}
