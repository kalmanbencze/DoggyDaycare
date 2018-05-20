package me.kalmanbncz.doggydaycare.presentation.browse.dogs;

import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
class DogItemViewModel extends ItemViewModel<Dog> {

    private final BrowseNavigator browseNavigator;

    private Dog dog;

    DogItemViewModel(BrowseNavigator browseNavigator) {
        this.browseNavigator = browseNavigator;
    }

    @Override
    public Dog getItem() {
        return dog;
    }

    @Override
    public void setItem(Dog dog) {
        this.dog = dog;
    }

    @Override
    public void onClick() {
        browseNavigator.openEdit(dog);
    }
}
