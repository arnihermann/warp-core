package com.wideplay.warp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * On: 25/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class DwrServletConfigAdapter implements ServletConfig {
    private final ServletContext servletContext;

    public DwrServletConfigAdapter(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String getInitParameter(String string) {
        return servletContext.getInitParameter(string);
    }

    public Enumeration getInitParameterNames() {
        return servletContext.getInitParameterNames();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getServletName() {
        return "dwr-invoker";   //change to warp-dwr invoker??
    }
}
