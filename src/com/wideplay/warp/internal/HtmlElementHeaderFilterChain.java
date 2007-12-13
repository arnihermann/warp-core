package com.wideplay.warp.internal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.wideplay.warp.rendering.templating.HtmlElement;
import com.wideplay.warp.rendering.templating.HtmlElementFilter;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 9, 2007
 * Time: 4:55:15 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
@Singleton
class HtmlElementHeaderFilterChain implements HtmlElementFilter {
    private final HtmlElementFilterKeys filters;
    private final Injector injector;

    @Inject
    HtmlElementHeaderFilterChain(HtmlElementFilterKeys filters, Injector injector) {
        this.filters = filters;
        this.injector = injector;
    }


    public HtmlElement filter(HtmlElement element) {
        //filter across all registered header filters
        for (Key<? extends HtmlElementFilter> key : filters.getKeys()) {
            element = injector.getInstance(key)
                    .filter(element);
        }
        return element;
    }
}
