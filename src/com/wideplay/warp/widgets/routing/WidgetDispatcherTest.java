package com.wideplay.warp.widgets.routing;

import org.testng.annotations.Test;
import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.wideplay.warp.widgets.Respond;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetDispatcherTest {
    @Test
    public final void dispatchRequestAndResponse() throws IOException {
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final Writer writer = createMock(Writer.class);

        

        final String[] output = new String[1];
        @SuppressWarnings("deprecation")
        final HttpServletResponse response = new HttpServletResponseWrapper(null) {
            @Override
            public PrintWriter getWriter() throws IOException {
                return new PrintWriter(System.out) {
                    @Override
                    public void write(String s) {
                        output[0] = s;
                    }
                };
            }
        };

        replay(request, writer);

        final Injector injector = Guice.createInjector();
        new WidgetDispatcher(injector.getInstance(PageBook.class), injector.getProvider(Respond.class))
                .dispatch(request, response);

        assert
    }
}
