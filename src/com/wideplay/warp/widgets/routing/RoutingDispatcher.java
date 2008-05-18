package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.Respond;
import com.google.inject.ImplementedBy;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ImplementedBy(WidgetRoutingDispatcher.class)
public interface RoutingDispatcher {
    Respond dispatch(HttpServletRequest request);
}
