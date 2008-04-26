package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.routing.PageBook;
import com.google.inject.Inject;
import com.google.inject.matcher.Matchers;
import static com.google.inject.matcher.Matchers.*;

import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class PageWidgetBuilder {
    private final ClassLister lister;
    private final PageBook pageBook;
    private final TemplateLoader loader;
    private final XmlTemplateParser parser;

    @Inject
    PageWidgetBuilder(ClassLister lister, PageBook pageBook, TemplateLoader loader, XmlTemplateParser parser) {
        this.lister = lister;
        this.pageBook = pageBook;
        this.loader = loader;
        this.parser = parser;
    }

    public void scan(Package target) {
        final Set<Class<?>> set = lister.list(target, annotatedWith(At.class));

        for (Class<?> page : set) {
            final RenderableWidget widget = parser.parse(loader.load(page));

            pageBook.at(page.getAnnotation(At.class).value(), widget, page);
        }
    }
}
