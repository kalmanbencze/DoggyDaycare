package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
abstract class ItemViewModel<ITEM_T> {

    public abstract ITEM_T getItem();

    public abstract void setItem(ITEM_T item);

    public abstract void onClick();
}
