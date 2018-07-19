package com.zwq65.unity;

import org.junit.Test;

/**
 * ================================================
 * <p>
 * Created by NIRVANA on 2018/7/9
 * Contact with <zwq651406441@gmail.com>
 * ================================================
 */
public class NormalTest {

    @Test
    public void numJewelsInStones() {
        String J = "a";
        String S = "aAAbbbb";
        int diff = 0;
        for (int i = 0; i < J.length(); i++) {
            for (int j = 0; j < S.length(); j++) {
                if (J.charAt(i) == S.charAt(j)) {
                    diff++;
                }
            }
        }
        System.out.println(diff);

    }
}
