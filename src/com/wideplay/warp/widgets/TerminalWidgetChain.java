package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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