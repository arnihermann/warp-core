package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class WidgetChain implements RenderableWidget {
    private final List<RenderableWidget> widgets = new ArrayList<RenderableWidget>();

    public void render(Object bound, Respond respond) {
        for (RenderableWidget widget : widgets) {
            widget.render(bound, respond);
        }
    }

    public WidgetChain addWidget(RenderableWidget renderableWidget) {
        widgets.add(renderableWidget);
        return this;
    }
}
