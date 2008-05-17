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
    private final Set<Package> packages;

    @Inject
    PageWidgetBuilder(PageBook pageBook, TemplateLoader loader, XmlTemplateParser parser, @Packages Set<Package> packages) {
        this.pageBook = pageBook;
        this.loader = loader;
        this.parser = parser;
        this.packages = packages;
    }

    public void scan(ServletContext context) {

        for (Package pack : packages) {

            //list any classes annotated with @At, @EmbedAs and @Export
            final Set<Class<?>> set = new ClassLister(context)
                                        .list(pack, annotatedWith(At.class));

            //now iterate and build widgets and store them (or whatever)
            for (Class<?> page : set) {
                final RenderableWidget widget = parser.parse(loader.load(page));

                pageBook.at(page.getAnnotation(At.class).value(), widget, page);
            }
        }
    }
}
