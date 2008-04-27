package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.rendering.EmbedAs;
import net.jcip.annotations.Immutable;

import java.util.Collection;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
@EmbedAs("Repeat")
class RepeatWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final String expression;
    private final Evaluator evaluator;

    public RepeatWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
        this.widgetChain = widgetChain;
        this.expression = expression;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        Collection<?> things = (Collection<?>) evaluator.evaluate(expression, bound);

        //noinspection UnusedDeclaration
        for (Object thing : things) {
            widgetChain.render(bound, respond);
        }
    }
}
