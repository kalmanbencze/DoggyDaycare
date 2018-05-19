package me.kalmanbncz.doggydaycare.presentation;

import android.support.v7.widget.RecyclerView;
import io.reactivex.Observable;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public abstract class RecyclerViewViewModel implements BaseViewModel {

    protected abstract ItemAdapter getAdapter();

    protected abstract RecyclerView.LayoutManager createLayoutManager();

    public abstract Observable<Boolean> getLoadingState();

    public abstract void loadMoreItems();
}