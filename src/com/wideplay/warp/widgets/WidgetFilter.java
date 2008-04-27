package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.routing.RoutingDispatcher;
import com.google.inject.Inject;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public final class WidgetFilter implements Filter {
    private final RoutingDispatcher dispatcher;

    @Inject
    public WidgetFilter(RoutingDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //dispatch
        final Respond respond = dispatcher.dispatch(request);

        if (null != respond)
            response.getWriter().write(respond.toString());
        else
            //continue down filter-chain
            filterChain.doFilter(request, response);
    }

    public void destroy() {
    }
}
