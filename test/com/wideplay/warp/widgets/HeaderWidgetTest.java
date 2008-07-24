package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;
import com.wideplay.warp.widgets.rendering.MvelEvaluatorCompiler;
import static org.easymock.EasyMock.createNiceMock;
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
    public final void renderHeaderWithContent() throws ExpressionCompileException {

        Respond respond = new StringBuilderRespond();

        final WidgetChain widgetChain = new WidgetChain();
        final EvaluatorCompiler mock = new MvelEvaluatorCompiler(Object.class);
        widgetChain.addWidget(new TextWidget("<meta name=\"thing\"/>", mock));

        new HeaderWidget(widgetChain, "", createNiceMock(Evaluator.class))
                .render(new Object(), respond);

        respond.writeToHead("<title>bs</title>");

        final String response = respond.toString();
        assert "<head><meta name=\"thing\"/><title>bs</title></head>".equals(response) :
                "instead printed: " + response;
    }
}