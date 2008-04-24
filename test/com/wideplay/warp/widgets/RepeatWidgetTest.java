package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.util.Collection;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class RepeatWidgetTest {
    private static final String LISTS_AND_TIMES = "listsAndTimes";

    @DataProvider(name = LISTS_AND_TIMES)
    public Object[][] getlistsAndTimes() {
        return new Object[][] {
            { 5, Arrays.asList(1,2,3,4,4) },
            { 4, Arrays.asList(1,2,3,4) },
            { 16, Arrays.asList(1,2,3,2,2,2,2,1,2,1,2,1,2,1,2,2) },
            { 0, Arrays.asList() },
        };
    }

    @Test(dataProvider = LISTS_AND_TIMES)
    public final void repeatNumberOfTimes(int should, final Collection<Integer> ints) {

        final int[] times = new int[1];
        final WidgetChain mockChain = new WidgetChain() {
            @Override
            public void render(Object bound, Respond respond) {
                times[0]++;
            }
        };


        new RepeatWidget(mockChain, "beans", new MvelEvaluator())
                .render(new HashMap<String, Object>() {{
                    put("beans", ints);
                }}, new StringBuilderRespond());

        assert times[0] == should : "Did not run expected number of times: " + should;
    }
}
