package com.wideplay.warp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created with IntelliJ IDEA.
 * On: 25/08/2007
 *
 * We need to do all kinds of hacks to getValue DWR to behave under the covers.
 * Here we are substituting the servlet path with a dwr-friendly expectation and
 * rewriting pathinfo to ignore the /dwr prefix.
 *
 * This way, warp can fire requests to dwr as though being received at / but really
 * it happens inside /[WARP-APP-CONTEXT]/dwr/ but this is transparent to the DWR engine.
 *
 * Note that the javascript-generated URLs still behave correctly (due to overriding of servlet path
 * and stripping of path info on the way in).
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@SuppressWarnings("deprecation")
class DwrRequestWrapper extends HttpServletRequestWrapper {
    private final String contextualUri;

    public DwrRequestWrapper(HttpServletRequest httpServletRequest, String contextualUri) {
        super(httpServletRequest);

        this.contextualUri = contextualUri;
    }

    @Override
    public String getServletPath() {
        return DwrWarpAdapter.DWR_PREFIX;
    }

    @Override
    public String getPathInfo() {
        return contextualUri.substring(DwrWarpAdapter.DWR_PREFIX.length());
    }

    static DwrRequestWrapper wrapRequestForDwr(HttpServletRequest request, String contextualUri) {
        return new DwrRequestWrapper(request, contextualUri);
    }
}
