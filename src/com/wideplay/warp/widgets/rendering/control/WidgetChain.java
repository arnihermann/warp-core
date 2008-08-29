package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Renderable;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public interface WidgetChain extends Renderable {
    WidgetChain addWidget(Renderable renderable);
}
