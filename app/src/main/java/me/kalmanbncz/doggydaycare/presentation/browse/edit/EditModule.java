package me.kalmanbncz.doggydaycare.presentation.browse.edit;

import me.kalmanbncz.doggydaycare.data.Dog;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 5/20/2018.
 */
public class EditModule extends Module {

    public EditModule(Dog dog) {
        bind(Dog.class).toInstance(dog);
        bind(EditViewModel.class).to(EditViewModelImpl.class);
    }
}
