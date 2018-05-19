package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import android.support.v7.util.DiffUtil;
import java.util.List;
import me.kalmanbncz.doggydaycare.data.Dog;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
class DogsDiffCallback extends DiffUtil.Callback {

    private List<Dog> oldDogs;

    private List<Dog> newDogs;

    DogsDiffCallback(List<Dog> newDogs, List<Dog> oldDogs) {
        this.newDogs = newDogs;
        this.oldDogs = oldDogs;
    }

    @Override
    public int getOldListSize() {
        return oldDogs.size();
    }

    @Override
    public int getNewListSize() {
        return newDogs.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDogs.get(oldItemPosition).getId() == newDogs.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDogs.get(oldItemPosition).getId() == newDogs.get(newItemPosition).getId();
    }
}
