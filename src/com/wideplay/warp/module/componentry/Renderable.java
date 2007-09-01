package com.wideplay.warp.module.componentry;

import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.RenderingContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface Renderable {
    void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents);
}
