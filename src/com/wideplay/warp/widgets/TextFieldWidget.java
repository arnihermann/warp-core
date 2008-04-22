package com.wideplay.warp.widgets;

import net.jcip.annotations.Immutable;
import com.wideplay.warp.widgets.rendering.SelfRendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
@SelfRendering
class TextFieldWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final String expression;
    private final Evaluator evaluator;

    public TextFieldWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
        this.widgetChain = widgetChain;
        this.expression = expression;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        respond.withHtml()
                .textField(expression, (String) evaluator.evaluate(expression, bound));
    }
}
