package com.wideplay.warp.widgets;

import net.jcip.annotations.Immutable;
import com.wideplay.warp.widgets.rendering.EmbedAs;


/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @EmbedAs("ShowIf")
class ShowIfWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final String expression;
    private final Evaluator evaluator;

    public ShowIfWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
        this.widgetChain = widgetChain;
        this.expression = expression;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        //messy =(
        final Object o = evaluator.evaluate(expression, bound);

        if ((Boolean) o)
            widgetChain.render(bound, respond);
    }
}
