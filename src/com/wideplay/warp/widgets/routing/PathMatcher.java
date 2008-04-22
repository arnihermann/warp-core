package com.wideplay.warp.widgets.routing;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
interface PathMatcher {
    boolean matches(String incoming);

    String name();
}
