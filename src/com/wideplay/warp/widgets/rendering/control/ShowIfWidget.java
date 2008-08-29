package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import net.jcip.annotations.Immutable;

import java.util.Set;


/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @EmbedAs("ShowIf")
class ShowIfWidget implements Renderable {
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


    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return widgetChain.collect(clazz);
    }
}
