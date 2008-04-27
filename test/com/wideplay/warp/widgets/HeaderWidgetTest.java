package com.wideplay.warp.widgets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class HeaderWidgetTest {
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

    @Test
    public final void renderHeader() {

        Respond respond = new StringBuilderRespond();

        new HeaderWidget(new WidgetChain(), "", new MvelEvaluator())
                .render(new Object(), respond);

        respond.writeToHead("<title>bs</title>");

        final String response = respond.toString();
        assert "<head><title>bs</title></head>".equals(response) :
                "instead printed: " + response;
    }

    @Test
    public final void renderHeaderWithContent() {

        Respond respond = new StringBuilderRespond();

        final WidgetChain widgetChain = new WidgetChain();
        final MvelEvaluator evaluator = new MvelEvaluator();
        widgetChain.addWidget(new TextWidget("<meta name=\"thing\"/>", evaluator));

        new HeaderWidget(widgetChain, "", evaluator)
                .render(new Object(), respond);

        respond.writeToHead("<title>bs</title>");

        final String response = respond.toString();
        assert "<head><meta name=\"thing\"/><title>bs</title></head>".equals(response) :
                "instead printed: " + response;
    }
}