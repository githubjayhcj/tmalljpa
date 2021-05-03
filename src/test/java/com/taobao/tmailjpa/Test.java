package com.taobao.tmailjpa;

import com.taobao.tmalljpa.util.ToolClass;

import java.math.BigDecimal;

public class Test {
    @org.junit.Test
    public void name() {
        BigDecimal b1 = new BigDecimal(0);
        BigDecimal b2 = new BigDecimal(0.35);
        for (int i = 0;i<2;i++){
            b1 = b1.add(b2);
        }
        ToolClass.out(b1);
    }
}
