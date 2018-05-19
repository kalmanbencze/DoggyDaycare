package me.kalmanbncz.doggydaycare.presentation;

import io.reactivex.Observable;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public abstract class RecyclerViewViewModel {

    public abstract Observable<Boolean> getLoadingState();

    public abstract void loadMoreItems();
}