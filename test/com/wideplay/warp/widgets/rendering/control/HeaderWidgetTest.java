package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.MvelEvaluator;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.RespondersForTesting;
import com.wideplay.warp.widgets.compiler.EvaluatorCompiler;
import com.wideplay.warp.widgets.compiler.ExpressionCompileException;
import com.wideplay.warp.widgets.compiler.MvelEvaluatorCompiler;
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

        Respond respond = RespondersForTesting.newRespond();

        new HeaderWidget(new ProceedingWidgetChain(), "", new MvelEvaluator())
                .render(new Object(), respond);

        respond.writeToHead("<title>bs</title>");

        final String response = respond.toString();
        assert "<head><title>bs</title></head>".equals(response) :
                "instead printed: " + response;
    }

    @Test
    public final void renderHeaderWithContent() throws ExpressionCompileException {

        Respond respond = RespondersForTesting.newRespond();

        final WidgetChain widgetChain = new ProceedingWidgetChain();
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