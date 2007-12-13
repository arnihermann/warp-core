package com.wideplay.warp.internal;

import com.google.inject.Key;
import com.wideplay.warp.rendering.templating.HtmlElementFilter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 13, 2007
 * Time: 4:43:25 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
class HtmlElementFilterKeys {
    private final List<Key<? extends HtmlElementFilter>> filters;

    HtmlElementFilterKeys(List<Key<? extends HtmlElementFilter>> filters) {
        this.filters = filters;
    }

    public List<Key<? extends HtmlElementFilter>> getKeys() {
        return filters;
    }
}
