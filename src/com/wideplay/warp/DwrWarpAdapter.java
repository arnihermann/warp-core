package com.wideplay.warp;

import com.google.inject.Injector;
import org.directwebremoting.guice.DwrGuiceServlet;
import org.directwebremoting.guice.DwrScopes;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * On: 21/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class DwrWarpAdapter {
    private final DwrGuiceServlet dwrServlet = new DwrGuiceServlet();
    private static final String INJECTOR = "org.directwebremoting.guice.Injector";
    static final String DWR_PREFIX = "/dwr";


    protected void configure() {
    }

    public void start(final ServletContext servletContext, Injector injector) throws ServletException {
        servletContext.setAttribute(INJECTOR, injector);

        dwrServlet.init(new DwrServletConfigAdapter(servletContext));
    }

    public boolean isDwrServiceable(String uri) {
        return uri.startsWith(DwrWarpAdapter.DWR_PREFIX);
    }

    public void service(String contextualUri, HttpServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        dwrServlet.service(DwrRequestWrapper.wrapRequestForDwr(servletRequest, contextualUri), servletResponse);
    }

    public void shutdown(final ServletContext servletContext) {
        //shutdown servlet first
        DwrScopes.APPLICATION.closeAll();
        dwrServlet.destroy();
    }
}
