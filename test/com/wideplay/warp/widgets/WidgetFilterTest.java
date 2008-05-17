package com.wideplay.warp.widgets;

import com.google.inject.Provider;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
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

    @Test
    public final void init() throws ServletException {
        final PageBook pageBook = createNiceMock(PageBook.class);
        final Evaluator evaluator = createNiceMock(Evaluator.class);

        final int[] adds = new int[1];
        final WidgetRegistry registry = new WidgetRegistry(evaluator, pageBook) {
            @Override
            public void add(String key, Class<? extends RenderableWidget> widget) {
                adds[0]++;
            }
        };

        final boolean ran[] = new boolean[1];
        final PageWidgetBuilder pageWidgetBuilder = new PageWidgetBuilder(pageBook, new TemplateLoader(),
                new XmlTemplateParser(evaluator, registry), new HashSet<Package>()) {

            @Override
            public void scan(ServletContext context) {
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
        Respond respond = mockRespond();

        expect(dispatcher.dispatch(request))
                .andReturn(respond);

//        filterChain.doFilter(request, response);
//        expectLastCall().once();

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

    private Respond mockRespond() {
        //noinspection OverlyComplexAnonymousInnerClass
        return new Respond() {
            public void write(String text) {
            }

            public HtmlTagBuilder withHtml() {
                return null;
            }

            public void write(char c) {
            }

            public void chew() {
            }

            public void writeToHead(String text) {
            }

            public void require(String requireString) {
                
            }

            public void redirect(String to) {
                
            }

            @Override
            public String toString() {
                return SOME_OUTPUT;
            }
        };
    }
}
