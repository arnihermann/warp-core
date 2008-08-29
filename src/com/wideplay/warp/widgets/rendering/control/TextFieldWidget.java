package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.rendering.SelfRendering;
import net.jcip.annotations.Immutable;

import java.util.Collections;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
@SelfRendering
class TextFieldWidget implements Renderable {
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

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }
}
