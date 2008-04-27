package com.wideplay.warp.widgets.routing;

import org.testng.annotations.Test;
import static org.easymock.EasyMock.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.google.inject.Provider;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.RenderableWidget;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetDispatcherTest {
    @Test
    public final void dispatchRequestAndRespond() throws IOException {
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse mockResponse = createMock(HttpServletResponse.class);
        PageBook pageBook = createMock(PageBook.class);
        PageBook.Page page = createMock(PageBook.Page.class);
        RenderableWidget widget = createMock(RenderableWidget.class);
        final Respond respond = createMock(Respond.class);

        Object pageOb = new Object();

        expect(request.getPathInfo())
                .andReturn("/thing");

        expect(pageBook.get("/thing"))
                .andReturn(page);

        expect(page.widget())
                .andReturn(widget);

        expect(page.instantiate())
                .andReturn(pageOb);

        widget.render(pageOb, respond);
        expectLastCall().once();


        final String[] output = new String[1];
        @SuppressWarnings("deprecation")
        final HttpServletResponse response = new HttpServletResponseWrapper(mockResponse) {
            @Override
            public PrintWriter getWriter() throws IOException {
                //noinspection InnerClassTooDeeplyNested
                return new PrintWriter(System.out) {
                    @Override
                    public void write(String s) {
                        output[0] = s;
                    }
                };
            }
        };

        replay(request, page, pageBook, widget, respond);

        Respond out = new WidgetRoutingDispatcher(pageBook, new Provider<Respond>() {
            public Respond get() {
                return respond;
            }
        }).dispatch(request);


        assert out == respond : "Did not respond correctly";
        
        verify(request, page, pageBook, widget, respond);

    }
}
