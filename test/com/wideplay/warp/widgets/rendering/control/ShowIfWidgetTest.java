package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.MvelEvaluator;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.RespondersForTesting;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        WidgetChain mockChain = new ProceedingWidgetChain() {
            @Override
            public void render(Object bound, Respond respond) {
                run[0] = true;
            }
        };


        //try to render widget
        new ShowIfWidget(mockChain, expression, new MvelEvaluator())
                .render(new HashMap<String, Object>() {{
                    put("visible", true);
                }}, RespondersForTesting.newRespond());

        assert run[0] == should : "ShowIf did not do as it should " + should;
    }
}
