package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import static com.google.inject.matcher.Matchers.*;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.resources.ResourcesService;
import com.wideplay.warp.widgets.resources.Export;
import com.wideplay.warp.widgets.resources.Assets;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.wideplay.warp.widgets.rendering.CallWith;
import com.wideplay.warp.widgets.aplenty.CaseWidget;

import javax.servlet.ServletContext;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class PageWidgetBuilder {
    private final PageBook pageBook;
    private final TemplateLoader loader;
    private final XmlTemplateParser parser;
    private final Set<Package> packages;
    private final ResourcesService resourcesService;
    private final Provider<ServletContext> context;
    private final WidgetRegistry registry;

    @Inject
    PageWidgetBuilder(PageBook pageBook, TemplateLoader loader,
                      XmlTemplateParser parser,
                      @Packages Set<Package> packages,
                      ResourcesService resourcesService,
                      Provider<ServletContext> servletContextProvider,
                      WidgetRegistry registry) {

        this.pageBook = pageBook;
        this.loader = loader;
        this.parser = parser;
        this.packages = packages;
        this.resourcesService = resourcesService;
        this.context = servletContextProvider;
        this.registry = registry;
    }

    public void scan() {
        //additional core widgets
        embed("case", CaseWidget.class);
        
        for (Package pack : packages) {

            //list any classes annotated with @At, @EmbedAs and @Export
            final Set<Class<?>> set = new ClassLister(context.get())
                                        .list(pack,
                                                annotatedWith(At.class)
                                                .or(annotatedWith(EmbedAs.class).or(annotatedWith(CallWith.class))
                                                .or(annotatedWith(Export.class))
                                                .or(annotatedWith(Assets.class))
                                                        
                                                ));

            //we need to store the embeds first (do not collapse into the next loop)
            for (Class<?> page : set) {
                if (page.isAnnotationPresent(EmbedAs.class)) {
                    final String embedAs = page.getAnnotation(EmbedAs.class).value();

                    //is this a text rendering or embedding-style widget?
                    if (Renderable.class.isAssignableFrom(page)) {
                        //noinspection unchecked
                        registry.add(embedAs, (Class<? extends Renderable>) page);
                    } else {
                        embed(embedAs, page);
                    }
                }
            }

            //now iterate and build widgets and store them (or whatever)
            for (Class<?> page : set) {

                if (page.isAnnotationPresent(At.class)) {
                    final Renderable widget = parser.parse(loader.load(page));
                    pageBook.at(page.getAnnotation(At.class).value(), widget, page);
                }

                if (page.isAnnotationPresent(Export.class)) {
                    resourcesService.add(page, page.getAnnotation(Export.class));
                }

                if (page.isAnnotationPresent(Assets.class)) {
                    for (Export export : page.getAnnotation(Assets.class).value())
                        resourcesService.add(page, export);
                }
            }
        }

    }

    private void embed(String embedAs, Class<?> page) {
        //store custom page wrapped as an embed widget
        registry.add(embedAs, EmbedWidget.class);

        //store argument name(s) wrapped as an ArgumentWidget (multiple aliases allowed)
        if (page.isAnnotationPresent(CallWith.class))
            for (String callWith : page.getAnnotation(CallWith.class).value())
                registry.add(callWith, ArgumentWidget.class);

        //...add as an unbound (to URI) widget
        pageBook.embedAs(parser.parse(loader.load(page)), page);
    }
}
