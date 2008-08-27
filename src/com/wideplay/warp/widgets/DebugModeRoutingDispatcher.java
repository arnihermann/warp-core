package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
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

    @Inject
    public DebugModeRoutingDispatcher(@Production RoutingDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }


    public Respond dispatch(HttpServletRequest request) {
        try {
            return dispatcher.dispatch(request);
        } catch (TemplateCompileException tce) {
            final StringBuilderRespond respond = new StringBuilderRespond();

            final Throwable cause = tce.getCause();

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
        }
    }
}
