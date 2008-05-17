package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import static com.google.inject.matcher.Matchers.annotatedWith;
import com.wideplay.warp.widgets.routing.PageBook;

import javax.servlet.ServletContext;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class PageWidgetBuilder {
    private final PageBook pageBook;
    private final TemplateLoader loader;
    private final XmlTemplateParser parser;

    @Inject
    PageWidgetBuilder(PageBook pageBook, TemplateLoader loader, XmlTemplateParser parser) {
        this.pageBook = pageBook;
        this.loader = loader;
        this.parser = parser;
    }

    public void scan(Package target, ServletContext context) {
        final Set<Class<?>> set = new ClassLister(context)
                    .list(target, annotatedWith(At.class));

        for (Class<?> page : set) {
            final RenderableWidget widget = parser.parse(loader.load(page));

            pageBook.at(page.getAnnotation(At.class).value(), widget, page);
        }
    }
}
