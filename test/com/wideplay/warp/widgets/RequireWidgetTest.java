package com.wideplay.warp.widgets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class RequireWidgetTest {
    private static final String REQUIRE_TAGS = "requireTags";

    @DataProvider(name = REQUIRE_TAGS)
    public Object[][] getRequires() {
        return new Object[][] {
            { "<link rel='thing.css'/>" },
            { "<script type=\"text/javascript\"/> @import 'thing.css' javascript dude bleod </script>" },
        };
    }

    @Test(dataProvider = REQUIRE_TAGS)
    public final void requireWidgetsRenderToHeadTag(final String requireString) {
        final Respond respond = new StringBuilderRespond();

        respond.require(requireString);
        respond.require(requireString);

        final MvelEvaluator evaluator = new MvelEvaluator();

        WidgetChain chain = new WidgetChain();

        chain.addWidget(new HeaderWidget(new TerminalWidgetChain(), "", evaluator));

        chain.addWidget(new RequireWidget(requireString, evaluator));
        chain.addWidget(new RequireWidget(requireString, evaluator));
        chain.addWidget(new RequireWidget(requireString, evaluator));

        //render
        chain.render(new Object(), respond);

        final String expected = "<head>" + requireString + "</head>";
        final String output = respond.toString();
        assert expected.equals(output) : "Header not correctly rendered: " + output;
    }
}
