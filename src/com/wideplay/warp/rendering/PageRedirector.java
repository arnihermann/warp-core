package com.wideplay.warp.rendering;

import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.pages.event.Forward;
import com.wideplay.warp.module.pages.event.Redirect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * A utility class that encapsulates static functionality for either redirecting or forwarding to a returned page.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class PageRedirector {
    public static final String WARP_REDIRECTED_NEXT_PAGE_OBJECT = "__warpRedirectedNextPageObject";


    static void forwardOrRedirect(Object forward, HttpServletResponse response, HttpServletRequest request, WarpModuleAssembly assembly) {
        //see if this is a straight up redirect
        if (forward instanceof Redirect.URLRedirection) {
            urlRedirect(forward, response);
            return;
        }


        //see if this is a page-forward (i.e. server-side redirect)
        if (forward instanceof Forward.PageForwarding) {
            pageForward(forward, assembly, request, response);
            return;
        }


        //unwrap if it's wrapped in a Redirection
        if (forward instanceof Redirect.PageRedirection)
            forward = ((Redirect.PageRedirection)forward).getPage();


        //load the page uri
        pageRedirect(assembly, forward, request, response);
    }



    private static void pageRedirect(WarpModuleAssembly assembly, Object forward, HttpServletRequest request, HttpServletResponse response) {
        final Log log = LogFactory.getLog(PageRedirector.class);
        
        String forwardUri = assembly.resolvePageURI(forward);
        if (null == forwardUri)
            throw new PageRenderException("The object returned for forwarding was not a registered Warp Page class: " + forward.getClass().getName());

        //store the page in the session awaiting the next request
        request.getSession().setAttribute(WARP_REDIRECTED_NEXT_PAGE_OBJECT, forward);

        //redirect to the page's identity
        try {
            if (log.isTraceEnabled())
                log.trace(String.format("Sending client-side forward page redirect to: %s", forwardUri));
            response.sendRedirect(formatRequestUri(request, forwardUri));
        } catch (IOException e) {
            throw new PageRenderException("The action was unsuccessful because an IO error occurred while sending a redirect to: " + forwardUri);
        }
    }

    private static String formatRequestUri(HttpServletRequest request, String forwardUri) {
        return String.format("%s%s", request.getContextPath(), forwardUri);
    }


    private static void pageForward(Object forward, WarpModuleAssembly assembly, HttpServletRequest request, HttpServletResponse response) {
        final Log log = LogFactory.getLog(PageRedirector.class);

        Forward.PageForwarding forwardPage = (Forward.PageForwarding)forward;
        PageHandler forwardPageHandler = assembly.getPage(assembly.resolvePageURI(forwardPage.getPage()));

        if (log.isTraceEnabled())
            log.trace(String.format("Forwarding server-side to page: %s", forwardPage.getPage()));

        //process forward & return (events do not get forwarded)
        forwardPageHandler.handleRequest(request, response, assembly.getInjector(), forwardPage.getPage());
    }



    
    private static void urlRedirect(Object forward, HttpServletResponse response) {
        final Log log = LogFactory.getLog(PageRedirector.class);
        Redirect.URLRedirection redirection = ((Redirect.URLRedirection) forward);
        try {
            if (log.isTraceEnabled())
                log.trace(String.format("Sending client-side arbitrary redirect: %s", redirection.getUrl()));
            response.sendRedirect(redirection.getUrl());

        } catch (IOException e) {
            throw new PageRenderException("The action was unsuccessful because an IO error occurred while sending a redirect to: " + redirection.getUrl());
        }
    }
}
