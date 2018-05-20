package me.kalmanbncz.doggydaycare.presentation.browse;

import toothpick.config.Module;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class BrowseModule extends Module {

    public BrowseModule() {
        bind(BrowseNavigator.class).to(BrowseNavigatorImpl.class).singletonInScope();
        bind(BrowseNavigator.class).to(BrowseNavigatorImpl.class).singletonInScope();
        bind(BrowseNavigator.class).to(BrowseNavigatorImpl.class).singletonInScope();
    }
}
