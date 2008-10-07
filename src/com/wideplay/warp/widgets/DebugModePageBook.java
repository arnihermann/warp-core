package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wideplay.warp.servlet.RequestScoped;
import com.wideplay.warp.widgets.compiler.Compilers;
import com.wideplay.warp.widgets.compiler.Parsing;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.Production;
import com.wideplay.warp.widgets.routing.SystemMetrics;
import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * Used in the development stage to intercept the real pagebook so we can reload & recompile
 * templates on demand.
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ThreadSafe @Singleton
class DebugModePageBook implements PageBook {
    private final PageBook book;
    private final Provider<TemplateLoader> templateLoader;
    private final SystemMetrics metrics;
    private final Compilers compilers;
    private final Provider<Memo> memo;

    @Inject
    public DebugModePageBook(@Production PageBook book, Provider<TemplateLoader> templateLoader,
                             SystemMetrics metrics, Compilers compilers, Provider<Memo> memo) {

        this.book = book;
        this.templateLoader = templateLoader;
        this.metrics = metrics;
        this.compilers = compilers;
        this.memo = memo;
    }

    public Page at(String uri, Class<?> myPageClass) {
        return book.at(uri, myPageClass);
    }

    public Page get(String uri) {
        final Page page = book.get(uri);

        //reload template
        reload(uri, page);
        
        return page;
    }

    public Page forName(String name) {
        final Page page = book.forName(name);

        //reload template
        reload(name, page);

        return page;
    }

    public Page embedAs(Class<?> page) {
        return book.embedAs(page);
    }


    private void reload(String identifier, Page page) {

        //do nothing on the first pass (and skip static resources)
        if (null == page || !metrics.isActive())
            return;

        //ensure we reload only once per request, per identifier
        final Memo memo = this.memo.get();
        if (memo.uris.contains(identifier))
            return;

        //otherwise, remember that we already loaded this in this request
        memo.uris.add(identifier);


        final Class<?> pageClass = page.pageClass();

        final String template = templateLoader.get().load(pageClass);

        if (Parsing.treatAsXml(template))
            page.apply(compilers.compileXml(pageClass, template));
        else
            page.apply(compilers.compileFlat(pageClass, template));
    }

    @RequestScoped
    private static class Memo {
        private final Set<String> uris = new HashSet<String>();
    }
}
