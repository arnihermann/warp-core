package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;

import java.util.Collections;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
class IncludeWidget implements Renderable {
    private final String name;
    private final Evaluator evaluator;

    public IncludeWidget(WidgetChain chain, String name, Evaluator evaluator) {
        this.name = name;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        respond.include((String) evaluator.evaluate(name, bound))
                .render(bound, respond);
    }

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }
}
