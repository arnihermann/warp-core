package com.wideplay.warp.widgets.routing;

import com.google.inject.Provider;
import com.wideplay.warp.widgets.RenderableWidget;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.binding.RequestBinder;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetRoutingDispatcherTest {
    @Test
    public final void dispatchRequestAndRespondOnGet() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        PageBook pageBook = createMock(PageBook.class);
        PageBook.Page page = createMock(PageBook.Page.class);
        RenderableWidget widget = createMock(RenderableWidget.class);
        final Respond respond = createMock(Respond.class);
        RequestBinder binder = createMock(RequestBinder.class);

        Object pageOb = new Object() ;

        expect(request.getPathInfo())
                .andReturn("/thing");

        expect(pageBook.get("/thing"))
                .andReturn(page);

        binder.bind(request, pageOb);
        expectLastCall().once();

        expect(page.widget())
                .andReturn(widget);

        expect(page.instantiate())
                .andReturn(pageOb);

        expect(request.getMethod())
                .andReturn("GET");

        page.doGet(pageOb);
        expectLastCall().once();


        widget.render(pageOb, respond);
        expectLastCall().once();


        replay(request, page, pageBook, widget, respond, binder);

        Respond out = new WidgetRoutingDispatcher(pageBook, binder, new Provider<Respond>() {
            public Respond get() {
                return respond;
            }
        }).dispatch(request);


        assert out == respond : "Did not respond correctly";
        
        verify(request, page, pageBook, widget, respond, binder);

    }


    @Test
    public final void dispatchRequestAndRespondOnPost() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        PageBook pageBook = createMock(PageBook.class);
        PageBook.Page page = createMock(PageBook.Page.class);
        RenderableWidget widget = createMock(RenderableWidget.class);
        final Respond respond = createMock(Respond.class);
        RequestBinder binder = createMock(RequestBinder.class);

        Object pageOb = new Object() ;

        expect(request.getPathInfo())
                .andReturn("/thing");

        expect(pageBook.get("/thing"))
                .andReturn(page);

        binder.bind(request, pageOb);
        expectLastCall().once();

        expect(page.widget())
                .andReturn(widget);

        expect(page.instantiate())
                .andReturn(pageOb);

        expect(request.getMethod())
                .andReturn("POST");

        page.doPost(pageOb);
        expectLastCall().once();


        widget.render(pageOb, respond);
        expectLastCall().once();


        replay(request, page, pageBook, widget, respond, binder);

        Respond out = new WidgetRoutingDispatcher(pageBook, binder, new Provider<Respond>() {
            public Respond get() {
                return respond;
            }
        }).dispatch(request);


        assert out == respond : "Did not respond correctly";

        verify(request, page, pageBook, widget, respond, binder);

    }

    @Test
    public final void dispatchNothingBecuaseOfNoUriMatch() {
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        PageBook pageBook = createMock(PageBook.class);
        RequestBinder binder = createMock(RequestBinder.class);

        @SuppressWarnings("unchecked")
        Provider<Respond> respond = createMock(Provider.class);

        Object pageOb = new Object();

        expect(request.getPathInfo())
                .andReturn("/not_thing");

        expect(pageBook.get("/not_thing"))
                .andReturn(null);

        replay(request, pageBook, respond, binder);

        Respond out = new WidgetRoutingDispatcher(pageBook, binder, respond).dispatch(request);


        assert out == null : "Did not respond correctly";

        verify(request, pageBook, respond, binder);

    }
}
