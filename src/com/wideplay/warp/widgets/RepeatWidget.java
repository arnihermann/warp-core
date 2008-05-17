package com.wideplay.warp.widgets;

import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import net.jcip.annotations.Immutable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
@EmbedAs("Repeat")
class RepeatWidget implements RenderableWidget {
    private final WidgetChain widgetChain;
    private final String items;
    private final String var;
    private final String pageVar;
    private final Evaluator evaluator;
    
    private static final String DEFAULT_PAGEVAR = "__page";

    public RepeatWidget(WidgetChain widgetChain, String expression, Evaluator evaluator) {
        this.widgetChain = widgetChain;

        final Map<String,String> map = TextTools.toBindMap(expression);
        this.items = map.get("items");
        String var = map.get("var");

        if (null != var)
            this.var = stripQuotes(var);
        else
            this.var = null;

        //by default the page comes in as __page
        String pageVar = map.get("pageVar");
        if (null == pageVar)
            pageVar = DEFAULT_PAGEVAR;
        else
            pageVar = stripQuotes(pageVar);

        this.pageVar = pageVar;

        this.evaluator = evaluator;
    }

    private String stripQuotes(String var) {
        return var.substring(1, var.length() - 1);
    }

    public void render(Object bound, Respond respond) {
        Collection<?> things = (Collection<?>) evaluator.evaluate(items, bound);

        //do nothing if the collection is unavailable for some reason
        if (null == things)
            return;

        if (null != var) {
            Map<String, Object> context = new HashMap<String, Object>(3);

            //set up context variables
            for (Object thing : things) {

                //decorate with some context
                context.put(var, thing);
                context.put(pageVar, bound);
                widgetChain.render(context, respond);
            }
        } else
            for (Object thing : things) {
                widgetChain.render(thing, respond);
            }
    }


}
