package com.wideplay.warp.internal;

import org.testng.annotations.Test;
import org.mvel.MVEL;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 11, 2007
 * Time: 11:39:37 AM
 */
public class MVELTests {

    @Test
    public final void testMvelCollectionOperations() {
        Map<String, Object> map = new HashMap<String, Object>();
        String str = "asodkaosdk";
        int hash = str.hashCode();

        map.put("list", Arrays.asList(str));


        System.out.println(MVEL.eval("list", map));


        System.out.println(MVEL.eval(hash + " in list", map));
    }
}
