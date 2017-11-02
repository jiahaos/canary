package com.paicheya.common.date;


import com.jaf.tools.date.ApDateUtil;
import org.junit.Test;

import java.util.Date;

/**
 * Created by zhaozh on 16/11/23.
 */
public class ApDateUtilTest {

    @Test
    public void getNextTowDayEndAt() throws Exception {
        Date now = new Date();
        System.out.println(now);
        Date nextTowDayAt = ApDateUtil.getNextTowDayEndAt(now);

        System.out.println(nextTowDayAt);
    }

}