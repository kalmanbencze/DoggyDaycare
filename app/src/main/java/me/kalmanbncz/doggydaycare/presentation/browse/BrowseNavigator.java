package me.kalmanbncz.doggydaycare.presentation.browse;

import hugo.weaving.DebugLog;
import me.kalmanbncz.doggydaycare.data.Dog;
import me.kalmanbncz.doggydaycare.di.scopes.flow.BrowseFlowScope;
import me.kalmanbncz.doggydaycare.presentation.Navigator;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@DebugLog
@BrowseFlowScope
public interface BrowseNavigator extends Navigator {

    void openAuth();

    void openDogList();

    void openEdit(Dog dog);

    void back();

    void openAdd();
}
