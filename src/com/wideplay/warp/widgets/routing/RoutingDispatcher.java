package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.Respond;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public interface RoutingDispatcher {
    Respond dispatch(HttpServletRequest request);
}
