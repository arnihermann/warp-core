package com.wideplay.warp.widgets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.wideplay.warp.widgets.example.Wiki;
import com.wideplay.warp.widgets.resources.ResourcesService;
import com.wideplay.warp.widgets.routing.PageBook;
import static org.easymock.EasyMock.*;
import org.testng.annotations.Test;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class PageWidgetBuilderTest {

    @Test
    public final void scanPackageForPagesAndWidgets() {
        final Package target = Wiki.class.getPackage();

        final Injector injector = Guice.createInjector();
        final PageBook book = injector.getInstance(PageBook.class);
        final ResourcesService resourcesService = injector.getInstance(ResourcesService.class);

        final ServletContext mock = createMock(ServletContext.class);

        expect(mock.getResourcePaths("/WEB-INF/classes/com/wideplay/warp/widgets/example/"))
                .andReturn(new HashSet<String>(Arrays.asList(
                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/Wiki.class",
                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/Search.class",
                        "/WEB-INF/classes/com/wideplay/warp/widgets/example/ContentNegotiationExample.class"
                )));

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
                new XmlTemplateParser(evaluator, registry), packages, resourcesService, servletContextProvider, registry)
                
                .scan();

        assert null != book.get("/wiki/search");
        assert null != book.get("/wiki/page/bloogity");
        assert null != book.get("/aPage");
        assert null == book.get("/wiki/bloogity");

        assert null != resourcesService.serve("/my.js");
        assert null != resourcesService.serve("/your.js");
    }
}