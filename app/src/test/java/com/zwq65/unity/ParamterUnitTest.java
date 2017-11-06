package com.zwq65.unity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Parameterized.class)
public class ParamterUnitTest {

    private String num;

    public ParamterUnitTest(String num) {
        this.num = num;
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("begin test");
    }

    @Test
    public void name() throws Exception {
        assertEquals("4", num);
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("end test");
    }

    @Parameterized.Parameters
    public static Collection data() {
        return Arrays.asList("1", "2", "3", "4");
    }

}

