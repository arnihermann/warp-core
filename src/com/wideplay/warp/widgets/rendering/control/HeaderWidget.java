package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.rendering.SelfRendering;

import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@EmbedAs("Meta") @SelfRendering
class HeaderWidget implements Renderable {
    private final WidgetChain widgetChain;
    private final String expression;
    private final Evaluator evaluator;

//    private static final String PLACEHOLDER = "__w:w:HEADER_PLACEHOLDER:NOWRITE__";

    public HeaderWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
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


    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return widgetChain.collect(clazz);
    }
}
