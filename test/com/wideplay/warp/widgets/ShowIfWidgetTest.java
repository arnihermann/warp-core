package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.util.HashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class ShowIfWidgetTest {
    private static final String EXPRESSIONS_AND_EVALS = "expressionsAndEvals";

    @DataProvider(name = EXPRESSIONS_AND_EVALS)
    public Object[][] getExprs() {
        return new Object[][] {
            { "visible", true },
            { "!visible", false },
            { "true", true },
            { "false", false },
        };
    }

    @Test(dataProvider = EXPRESSIONS_AND_EVALS)
    public final void hideChildren(String expression, boolean should) {
        final boolean[] run = new boolean[1];
        WidgetChain mockChain = new WidgetChain() {
            @Override
            public void render(Object bound, Respond respond) {
                run[0] = true;
            }
        };


        //try to render widget
        new ShowIfWidget(mockChain, expression, new MvelEvaluator())
                .render(new HashMap<String, Object>() {{
                    put("visible", true);
                }}, new StringBufferRespond());

        assert run[0] == should : "ShowIf did not do as it should " + should;
    }
}
