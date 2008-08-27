package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
import com.wideplay.warp.widgets.routing.SystemMetrics;
import org.mvel.PropertyAccessException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * In debug mode, this dispatcher is used to intercept the production dispatcher and provide debug
 * services (such as the /debug page, and the friendly compile errors page).
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Singleton
class DebugModeRoutingDispatcher implements RoutingDispatcher {
    private final RoutingDispatcher dispatcher;
    private final SystemMetrics metrics;
    private final PageBook pageBook;

    @Inject
    public DebugModeRoutingDispatcher(@Production RoutingDispatcher dispatcher, SystemMetrics metrics,
                                      PageBook pageBook) {

        this.dispatcher = dispatcher;
        this.metrics = metrics;
        this.pageBook = pageBook;
    }


    public Respond dispatch(HttpServletRequest request) {
        long start = System.currentTimeMillis();
        PageBook.Page page = pageBook.get(request.getRequestURI());

        try {
            return dispatcher.dispatch(request);


        } catch (TemplateCompileException tce) {
            //WE DO NOT LOG ERROR METRICS HERE, AS THEY ARE BETTER HANDLED BY THE COMPILER

            final StringBuilderRespond respond = new StringBuilderRespond();

            respond.write("<h3>");
            respond.write("Compile errors in page");
            respond.write("</h3>");
            respond.write("<pre>");
            respond.write(tce.getMessage());
            respond.write("</pre>");
            respond.write("<br/>");
            respond.write("<br/>");
            respond.write("<br/>");

            return respond;


        } catch (PropertyAccessException pae) {
            final StringBuilderRespond respond = new StringBuilderRespond();

            final Throwable cause = pae.getCause();

            respond.write("<h3>");
            respond.write("Exception during page render");
            respond.write("</h3>");
            respond.write("<br/>");
            respond.write("<br/>");
            respond.write("<br/>");

            //analyze cause and construct detailed errors page
            if (cause instanceof InvocationTargetException) {
                InvocationTargetException ite = (InvocationTargetException) cause;

                //create ourselves a printwriter to provide error output into
                final StringWriter writer = new StringWriter();
                ite.getCause().printStackTrace(new PrintWriter(writer));

                respond.write("<h3>");
                respond.write("Exception during page render");
                respond.write("</h3>");
                respond.write("<pre>");
                respond.write(writer.toString());
                respond.write("</pre>");
            }

            return respond;
        } finally {
            long time = System.currentTimeMillis() - start;

            metrics.logPageRenderTime(page.pageClass(), time);
        }
    }
}
