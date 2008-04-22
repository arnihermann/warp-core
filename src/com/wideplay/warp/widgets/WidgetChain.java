package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

    public void addWidget(RenderableWidget renderableWidget) {
        widgets.add(renderableWidget);
    }
}
