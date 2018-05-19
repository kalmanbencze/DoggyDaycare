package me.kalmanbncz.doggydaycare.di;

import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigator;
import me.kalmanbncz.doggydaycare.presentation.browse.BrowseNavigatorImpl;
import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BrowseModule extends Module {

    public BrowseModule() {
        bind(BrowseNavigator.class).to(BrowseNavigatorImpl.class).singletonInScope();
    }
}
