package com.wideplay.warp.widgets;

import net.jcip.annotations.Immutable;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
class SingleWidgetChain extends WidgetChain {
    private final RenderableWidget widget;

    public SingleWidgetChain(RenderableWidget widget) {
        this.widget = widget;
    }

    public void render(Object bound, Respond respond) {
        widget.render(bound, respond);
    }

    public WidgetChain addWidget(RenderableWidget renderableWidget) {
        throw new IllegalStateException("Cannot add children to single widget chain");
    }
}