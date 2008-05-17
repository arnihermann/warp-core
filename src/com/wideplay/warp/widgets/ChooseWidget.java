package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.widgets.binding.FlashCache;
import com.wideplay.warp.widgets.rendering.SelfRendering;
import net.jcip.annotations.Immutable;

import java.util.Collection;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @SelfRendering
class ChooseWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final Map<String, String> map;
    private final Evaluator evaluator;

    private volatile Provider<FlashCache> cache;

    public ChooseWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
        this.evaluator = evaluator;
        this.map = TextTools.toBindMap(expression);
        this.widgetChain = widgetChain;

        //TODO validate expression
    }

    public void render(Object bound, Respond respond) {
        final String from = map.get("from");

        Object o = evaluator.read(from, bound);

        if (!(o instanceof Collection))
            throw new IllegalArgumentException("@Choose widget's from argument MUST be of type java.util.Collection " +
                    "but was: " + (null == o ? "null" : o.getClass()));

        respond.write("<select name=\"");
        respond.write(map.get("bind"));
        respond.write("\">");

        Collection<?> collection = (Collection<?>) o;
        for (Object obj : collection) {
            respond.write("<option value=\"[C/");
            respond.write(from);
            respond.write('/');
            respond.write(Integer.toString(obj.hashCode()));
            respond.write("\">");
        }

        respond.write("</select>");

        //store for later retrieval during binding
        cache.get().put(from, collection);
    }

    @Inject
    public void setCache(Provider<FlashCache> cache) {
        this.cache = cache;
    }
}
