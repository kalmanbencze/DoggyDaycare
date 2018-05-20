package me.kalmanbncz.doggydaycare.domain;

import org.junit.After;
import org.junit.Test;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DogsApiServiceTest {

    @After
    public void tearDown() throws Exception {
        Toothpick.reset();
    }

    @Test
    public void componentTest() throws Exception {
    }
}
