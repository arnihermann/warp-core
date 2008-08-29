package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import static com.google.inject.matcher.Matchers.annotatedWith;
import com.wideplay.warp.widgets.rendering.*;
import com.wideplay.warp.widgets.rendering.control.WidgetRegistry;
import com.wideplay.warp.widgets.rendering.resources.Assets;
import com.wideplay.warp.widgets.rendering.resources.Export;
import com.wideplay.warp.widgets.rendering.resources.ResourcesService;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.SystemMetrics;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class ClasspathWidgetScanner implements WidgetScanner {
    private final PageBook pageBook;
    private final TemplateLoader loader;
    private final Set<Package> packages;
    private final ResourcesService resourcesService;
    private final Provider<ServletContext> context;
    private final WidgetRegistry registry;
    private final SystemMetrics metrics;


    private final Logger log = Logger.getLogger(ClasspathWidgetScanner.class.getName());

    @Inject
    ClasspathWidgetScanner(PageBook pageBook, TemplateLoader loader,
                      @Packages Set<Package> packages,
                      ResourcesService resourcesService,
                      Provider<ServletContext> servletContextProvider,
                      WidgetRegistry registry, SystemMetrics metrics) {

        this.pageBook = pageBook;
        this.loader = loader;
        this.packages = packages;
        this.resourcesService = resourcesService;
        this.context = servletContextProvider;
        this.registry = registry;

        this.metrics = metrics;
    }

    public void scan() {

        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Package pkg : packages) {

            //list any classes annotated with @At, @EmbedAs and @Export
            set.addAll(Classes.matching(annotatedWith(At.class)
                    .or(annotatedWith(EmbedAs.class).or(annotatedWith(CallWith.class))
                    .or(annotatedWith(Embed.class)).or(annotatedWith(With.class))
                    .or(annotatedWith(Export.class))
                    .or(annotatedWith(Assets.class))

                )).in(pkg)
            );
        }

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


        //compile templates for scanned classes
        compilePages(pagesToCompile);

        //now iterate and build static resources
        for (Class<?> page : set) {

            if (page.isAnnotationPresent(Export.class)) {
                resourcesService.add(page, page.getAnnotation(Export.class));
            }

            if (page.isAnnotationPresent(Assets.class)) {
                for (Export export : page.getAnnotation(Assets.class).value())
                    resourcesService.add(page, export);
            }
        }


        //set application mode to started (now debug mechanics can kick in)
        metrics.activate();

    }

    private void compilePages(Set<PageBook.Page> pagesToCompile) {
        final List<TemplateCompileException> failures = new ArrayList<TemplateCompileException>();

        //perform a compilation pass over all the pages and their templates
        for (PageBook.Page toCompile : pagesToCompile) {
            Class<?> page = toCompile.pageClass();

            log.finest(String.format("Compiling template for page %s", page));

            try {
                final String template = loader.load(page);

                Renderable widget;

                //is this an XML template or a flat-file template?
                if (Parsing.treatAsXml(template))
                    widget = new XmlTemplateCompiler(
                            page,
                            new MvelEvaluatorCompiler(page),
                            registry,
                            pageBook,
                            metrics
                    )

                            .compile(template);
                else
                    widget = new FlatTemplateCompiler(
                            page,
                            new MvelEvaluatorCompiler(page),
                            metrics,
                            registry)

                            .compile(template);

                //apply the compiled widget chain to the page (completing compile step)
                toCompile.apply(widget);

            } catch (TemplateCompileException e) {
                failures.add(e);
            }
        }

        //log failures if any (we don't abort the app startup)
        if (!failures.isEmpty())
            logFailures(failures);

    }

    private PageBook.Page embed(String embedAs, Class<?> page) {
        //store custom page wrapped as an embed widget
        registry.addEmbed(embedAs);

        //store argument name(s) wrapped as an ArgumentWidget (multiple aliases allowed)
        if (page.isAnnotationPresent(CallWith.class))
            for (String callWith : page.getAnnotation(CallWith.class).value())
                registry.addArgument(callWith);


        //...add as an unbound (to URI) widget
        return pageBook.embedAs(page);
    }

    private void logFailures(List<TemplateCompileException> failures) {
        StringBuilder builder = new StringBuilder();
        builder.append("One or more templates could not be compiled. The following errors were detected:\n\n");

        for (TemplateCompileException failure : failures) {
            builder.append("Compile errors, ");
            builder.append(failure.getMessage());
            builder.append("\n\n");

            for (CompileError error : failure.getErrors()) {
                builder.append(error.toString());

                final EvaluatorCompiler.CompileErrorDetail errorDetail = error.getCause();
                if (null != errorDetail) {
                    builder.append(": ");
                    builder.append(errorDetail.getExpression());
                    builder.append("\n\n * ");
                    builder.append(errorDetail.getError().getMessage());
                }
            }

            builder.append("\n\nTotal errors: ");
            builder.append(failure.getErrors().size());

            if (!failure.getWarnings().isEmpty()) {

                builder.append("\n\nCompile warnings, \n\n");

                for (CompileError warning : failure.getWarnings()) {
                    builder.append(warning.toString());
                }

                builder.append("\n\nTotal warnings: ");
                builder.append(failure.getWarnings().size());
            }

            builder.append("\n\n");
        }

        log.severe(builder.toString());
    }
}
