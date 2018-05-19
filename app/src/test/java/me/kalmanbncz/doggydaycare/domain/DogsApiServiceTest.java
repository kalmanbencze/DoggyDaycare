package me.kalmanbncz.doggydaycare.domain;

import me.kalmanbncz.doggydaycare.BuildConfig;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DogsApiServiceTest {

    @After
    public void tearDown() throws Exception {
        Toothpick.reset();
    }

    @Test
    public void componentTest() throws Exception {
    }
}
