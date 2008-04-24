package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.rendering.SelfRendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@EmbedAs("Meta") @SelfRendering
class HeaderWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final String expression;
    private final MvelEvaluator evaluator;

    private static final String PLACEHOLDER = "__w:w:HEADER_PLACEHOLDER:NOWRITE__";

    public HeaderWidget(WidgetChain widgetChain, String expression, MvelEvaluator evaluator) {
        this.widgetChain = widgetChain;
        this.expression = expression;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        respond.write("<head>");

        //render children (as necessary)
        widgetChain.render(bound, respond);
        respond.withHtml()
                .headerPlaceholder();
        respond.write("</head>");
    }
}
