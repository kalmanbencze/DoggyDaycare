package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import java.util.List;
import me.kalmanbncz.doggydaycare.data.Breed;
import me.kalmanbncz.doggydaycare.data.Dog;

/**
 * Created by kalman.bencze on 5/19/2018.
 */
class DogAndBreedsHolder {

    Dog dog;

    List<Breed> breeds;

    DogAndBreedsHolder(Dog dog, List<Breed> breeds) {
        this.dog = dog;
        this.breeds = breeds;
    }

    public List<Breed> getBreeds() {
        return breeds;
    }

    public Dog getDog() {
        return dog;
    }
}
