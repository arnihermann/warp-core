package com.wideplay.warp.widgets;

import com.google.inject.Provider;
import com.google.inject.Injector;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
import com.wideplay.warp.widgets.resources.ResourcesService;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetFilterTest {
    private static final String SOME_OUTPUT = "some outputdaoskdasd__" + new Date();
    private static final String A_REDIRECT_LOCATION = "/a/redirect/location";
    private static final String TEXT_HTML = "text/html";

    @Test
    public final void init() throws ServletException {
        final PageBook pageBook = createNiceMock(PageBook.class);
        final Evaluator evaluator = createNiceMock(Evaluator.class);

        final int[] adds = new int[1];
        final WidgetRegistry registry = new WidgetRegistry(evaluator, pageBook, createNiceMock(Injector.class)) {
            @Override
            public void add(String key, Class<? extends RenderableWidget> widget) {
                adds[0]++;
            }
        };

        final boolean ran[] = new boolean[1];
        final PageWidgetBuilder pageWidgetBuilder = new PageWidgetBuilder(pageBook, new TemplateLoader(null),
                new XmlTemplateParser(evaluator, registry), new HashSet<Package>(), createNiceMock(ResourcesService.class), null, registry) {

            @Override
            public void scan() {
                ran[0] = true;
            }
        };

        final FilterConfig filterConfig = createMock(FilterConfig.class);

        expect(filterConfig.getServletContext())
                .andReturn(createMock(ServletContext.class));

        replay(filterConfig);

        new WidgetFilter(createNiceMock(RoutingDispatcher.class), new Provider<ContextInitializer>() {
            public ContextInitializer get() {
                return new ContextInitializer(pageWidgetBuilder, registry);
            }
        })
                .init(filterConfig);

//        assert ran[0] : "page widget builder scan method not called";
        assert adds[0] > 1 : "core widgets were not registered";

        verify(filterConfig);
    }

    @Test
    public final void doFilter() throws IOException, ServletException {
        RoutingDispatcher dispatcher = createMock(RoutingDispatcher.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        FilterChain filterChain = createMock(FilterChain.class);
        Respond respond = new StringBuilderRespond() {
            @Override
            public String toString() {
                return SOME_OUTPUT;
            }

            @Override
            public String getRedirect() {
                return null;
            }

            @Override
            public String getContentType() {
                return TEXT_HTML;
            }
        };

        expect(dispatcher.dispatch(request))
                .andReturn(respond);

        //nothing set?
        expect(response.getContentType())
                .andReturn(null);

        response.setContentType(TEXT_HTML);

        final boolean[] outOk = new boolean[2];
        final String[] output = new String[1];
        expect(response.getWriter())
                .andReturn(new PrintWriter(System.out) {
                    @Override
                    public void write(String s) {
                        outOk[0] = true;
                        output[0] = s;
                    }

                    @Override
                    public void flush() {
                        outOk[1] = true;
                    }
                });

        replay(dispatcher, request, response, filterChain);

        new WidgetFilter(dispatcher, null)
                .doFilter(request, response, filterChain);

        assert outOk[0] && !outOk[1] : "Response not written or flushed correctly";
        assert SOME_OUTPUT.equals(output[0]) : "Respond output not used";
        verify(dispatcher, request, response, filterChain);
    }


    @Test
    public final void doFilterDoesntHandleButProceedsDownChain() throws IOException, ServletException {
        RoutingDispatcher dispatcher = createMock(RoutingDispatcher.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        FilterChain filterChain = createMock(FilterChain.class);

        //meaning no dispatch could be performed...
        expect(dispatcher.dispatch(request))
                .andReturn(null);

        filterChain.doFilter(request, response);
        expectLastCall().once();

        replay(dispatcher, request, response, filterChain);

        new WidgetFilter(dispatcher, null)
                .doFilter(request, response, filterChain);

        verify(dispatcher, request, response, filterChain);
    }

    @Test
    public final void doFilterRedirects() throws IOException, ServletException {
        RoutingDispatcher dispatcher = createMock(RoutingDispatcher.class);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        FilterChain filterChain = createMock(FilterChain.class);

        Respond respond = createMock(Respond.class);

        //meaning no dispatch could be performed...
        expect(dispatcher.dispatch(request))
                .andReturn(respond);

        expect(respond.getRedirect())
                .andReturn(A_REDIRECT_LOCATION);

        response.sendRedirect(A_REDIRECT_LOCATION);

        replay(dispatcher, request, response, filterChain, respond);

        new WidgetFilter(dispatcher, null)
                .doFilter(request, response, filterChain);

        verify(dispatcher, request, response, filterChain, respond);
    }

}
