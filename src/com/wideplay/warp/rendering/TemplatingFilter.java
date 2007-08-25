package com.wideplay.warp.rendering;

import com.wideplay.warp.module.UriMatcher;
import com.wideplay.warp.module.WarpConfiguration;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.util.TextTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * An HTML templating filter for warp (generates dispatch type) 
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class TemplatingFilter {
    private final WarpModuleAssembly assembly;
    private final ServletContext context;

    private final Log log = LogFactory.getLog(TemplatingFilter.class);

    public TemplatingFilter(WarpModuleAssembly assembly, ServletContext context) {
        this.assembly = assembly;
        this.context = context;
    }

    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        //first check if this is a forwardOrRedirect action (and use the flash-scoped "next" page then)
        Object page = request.getSession().getAttribute(PageRedirector.WARP_REDIRECTED_NEXT_PAGE_OBJECT);

        //if it is, clear it
        request.getSession().setAttribute(PageRedirector.WARP_REDIRECTED_NEXT_PAGE_OBJECT, null);


        //locate page handler from uri and handle page
        final String contextualUri = TextTools.extractContextualUri(request);
        PageHandler handler = assembly.getUserFacingPage(contextualUri);

        String uriPart = null;
        if (null == handler) {
            UriMatcher.MatchTuple matchTuple = assembly.getPageUriMatch(contextualUri);

            //render normally?, i.e. thru servlet
            if (null == matchTuple)
                return false;

            handler = matchTuple.pageHandler;
            uriPart = matchTuple.uriExtract;

            //decode URI part if necessary
            if (null != uriPart)
                uriPart = decodeUriPart(uriPart);
        }


        if (log.isTraceEnabled())
            log.trace(String.format("Filter active for page: %s; handled by: %s", request.getRequestURI(), handler));


        //locate template and render to response
        response.setContentType("text/html");


        //get event id and dispatch with the appropriate page object
        Object forward = handler.handleRequest(request, response, assembly.getInjector(), page, uriPart);

        //do forward if necessary (really a client-side redirect)
        if (null != forward)
            PageRedirector.forwardOrRedirect(forward, response, request, assembly);

        //Filter was active, meaning Warp handled the request so return true (i.e. no need to continue down filter chain)
        return true;
    }

    //attempts to decode the (extracted) dynamic part of the URI using the configured encoding scheme
    private String decodeUriPart(String uriPart) {
        String scheme = assembly.getInjector().getInstance(WarpConfiguration.class).getUrlEncoding();
        
        try {
            return URLDecoder.decode(uriPart, scheme);
        } catch (UnsupportedEncodingException e) {
            throw new PageRenderException("Unable to decode dynamic URI part, encoding scheme was not supported: " + scheme, e);
        }
    }


}
