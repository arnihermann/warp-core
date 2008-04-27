package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

/**
 * <p>
 *
 *  Marker represents the end of a widget chain/branch
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class TerminalWidgetChain extends WidgetChain {

    public void render(Object bound, Respond respond) { }

    public void addWidget(RenderableWidget renderableWidget) { }
}