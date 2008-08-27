package com.wideplay.warp.widgets;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import static com.wideplay.warp.widgets.XmlTemplateCompilerTest.mockRequestProviderForContext;
import com.wideplay.warp.widgets.example.Wiki;
import com.wideplay.warp.widgets.resources.ResourcesService;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.SystemMetrics;
import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PageWidgetBuilderTest {
    private static final String SIMPLE_HTML = "<html><head></head><body> <p> hi</p></body></html>\n";

//    @Test
    public final void scanPackageForPagesAndWidgets() throws IOException {
        final Package target = Wiki.class.getPackage();

        final Injector injector = Guice.createInjector(new AbstractModule() {
            protected void configure() {
                //TODO cleanup this by factoring into a test util class?
                bind(HttpServletRequest.class).toProvider(mockRequestProviderForContext());
            }
        });
        final PageBook book = injector.getInstance(PageBook.class);
        final ResourcesService resourcesService = injector.getInstance(ResourcesService.class);

        final ServletContext mock = createMock(ServletContext.class);
//
//        expect(mock.getResourcePaths("/WEB-INF/classes/com/wideplay/warp/widgets/example/"))
//                .andReturn(new HashSet<String>(Arrays.asList(
//                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/Wiki.class",
//                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/Search.class",
//                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/ContentNegotiationExample.class"
//                )));

        expect(mock.getRealPath(isA(String.class))).andReturn("").anyTimes();

        expect(mock.getResourceAsStream(isA(String.class)))
                .andReturn(PageWidgetBuilderTest.class.getResourceAsStream("MyHtml.html")).anyTimes();

        replay(mock);

        final MvelEvaluator evaluator = new MvelEvaluator();
        final WidgetRegistry registry = new WidgetRegistry(evaluator, book, injector);

        //register core widgets (needed for scanning templates)
        registry.add("showif", ShowIfWidget.class);
        registry.add("repeat", RepeatWidget.class);
        registry.add("textfield", TextFieldWidget.class);
        registry.add("meta", HeaderWidget.class);
        registry.add("include", IncludeWidget.class);

        Set<Package> packages = new HashSet<Package>();
        packages.add(target);

        final Provider<ServletContext> servletContextProvider = new Provider<ServletContext>() {
            public ServletContext get() {
                return mock;
            }
        };
        
        new PageWidgetBuilder(book, new TemplateLoader(servletContextProvider),
                packages, resourcesService, servletContextProvider, registry, createNiceMock(SystemMetrics.class))
                
                .scan();

        assert null != book.get("/wiki/search");
        assert null != book.get("/wiki/page/bloogity");
        assert null != book.get("/aPage");
        assert null == book.get("/wiki/bloogity");

        assert null != resourcesService.serve("/my.js");
        assert null != resourcesService.serve("/your.js");
    }
}