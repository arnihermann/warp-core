package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import static com.google.inject.matcher.Matchers.annotatedWith;
import com.wideplay.warp.widgets.rendering.*;
import com.wideplay.warp.widgets.resources.Assets;
import com.wideplay.warp.widgets.resources.Export;
import com.wideplay.warp.widgets.resources.ResourcesService;
import com.wideplay.warp.widgets.routing.PageBook;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class PageWidgetBuilder {
    private final PageBook pageBook;
    private final TemplateLoader loader;
    private final Set<Package> packages;
    private final ResourcesService resourcesService;
    private final Provider<ServletContext> context;
    private final WidgetRegistry registry;

    @Inject
    PageWidgetBuilder(PageBook pageBook, TemplateLoader loader,
                      @Packages Set<Package> packages,
                      ResourcesService resourcesService,
                      Provider<ServletContext> servletContextProvider,
                      WidgetRegistry registry) {

        this.pageBook = pageBook;
        this.loader = loader;
        this.packages = packages;
        this.resourcesService = resourcesService;
        this.context = servletContextProvider;
        this.registry = registry;

    }

    public void scan() {
        
        for (Package pack : packages) {

            //list any classes annotated with @At, @EmbedAs and @Export
            final Set<Class<?>> set = Classes.matching(annotatedWith(At.class)
                                                .or(annotatedWith(EmbedAs.class).or(annotatedWith(CallWith.class))
                                                .or(annotatedWith(Embed.class)).or(annotatedWith(With.class))
                                                .or(annotatedWith(Export.class))
                                                .or(annotatedWith(Assets.class))
                                                        
                                                )).in(pack);

            //store a set of pages to compile
            Set<PageBook.Page> pagesToCompile = new HashSet<PageBook.Page>();

            //we need to store the pages first (do not collapse into the next loop)
            for (Class<?> page : set) {
                if (page.isAnnotationPresent(EmbedAs.class)) {
                    final String embedAs = page.getAnnotation(EmbedAs.class).value();

                    //is this a text rendering or embedding-style widget?
                    if (Renderable.class.isAssignableFrom(page)) {
                        //noinspection unchecked
                        registry.add(embedAs, (Class<? extends Renderable>) page);
                    } else {
                        pagesToCompile.add(embed(embedAs, page));
                    }
                }

                At at = page.getAnnotation(At.class);
                if (null != at)
                    pagesToCompile.add(pageBook.at(at.value(), page));
            }



            //perform a compilation pass over all the pages and their templates
            for (PageBook.Page toCompile : pagesToCompile) {
                Class<?> page = toCompile.pageClass();

                final Renderable widget = new XmlTemplateCompiler(page, new MvelEvaluatorCompiler(page),
                        registry, pageBook)
                        .compile(loader.load(page));

                //apply the compiled widget chain to the page (completing compile step)
                toCompile.apply(widget);
            }



            //now iterate and build widgets and store them (or whatever)
            for (Class<?> page : set) {

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

    private PageBook.Page embed(String embedAs, Class<?> page) {
        //store custom page wrapped as an embed widget
        registry.add(embedAs, EmbedWidget.class);

        //store argument name(s) wrapped as an ArgumentWidget (multiple aliases allowed)
        if (page.isAnnotationPresent(CallWith.class))
            for (String callWith : page.getAnnotation(CallWith.class).value())
                registry.add(callWith, ArgumentWidget.class);


        //...add as an unbound (to URI) widget
        return pageBook.embedAs(page);
    }
}
