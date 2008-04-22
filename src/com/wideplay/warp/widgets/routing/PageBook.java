package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.RenderableWidget;
import com.google.inject.Singleton;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * contains active uri/widget mappings
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Singleton @ThreadSafe
class PageBook {
    //multimaps TODO refactor to multimap?
    private final Map<String, List<PageTuple>> pages = new HashMap<String, List<PageTuple>>();
    private final List<PageTuple> universalMatchingPages = new ArrayList<PageTuple>();

    private final Object lock = new Object();

    public void at(String uri, RenderableWidget page) {
        synchronized (lock) {
            final String key = firstPathElement(uri);

            //is universal? (i.e. first element is a variable)
            if (isFirstElementVariable(key))
                universalMatchingPages.add(new PageTuple(new PathMatcherChain(uri), page));
            else {
                multiput(pages, key, new PageTuple(new PathMatcherChain(uri), page));
            }
        }
    }

    private static void multiput(Map<String, List<PageTuple>> pages, String key, PageTuple page) {
        List<PageTuple> list = pages.get(key);

        if (null == list) {
            list = new ArrayList<PageTuple>();
            pages.put(key, list);
        }

        list.add(page);
    }

    private static boolean isFirstElementVariable(String key) {
        return ':' == key.charAt(0);
    }

    String firstPathElement(String uri) {
        String shortUri = uri.substring(1);

        final int index = shortUri.indexOf("/");

        return (index >= 0) ? shortUri.substring(0, index) : shortUri;
    }

    public RenderableWidget get(String uri) {
        final String key = firstPathElement(uri);

        List<PageTuple> tuple = pages.get(key);

        //first try static first piece
        if (null != tuple) {

            //first try static first piece
            for (PageTuple pageTuple : tuple) {
                if (pageTuple.matcher.matches(uri))
                    return pageTuple.page;
            }
        }

        //now try dynamic first piece (how can we make this faster?)
        for (PageTuple pageTuple : universalMatchingPages) {
            if (pageTuple.matcher.matches(uri))
                return pageTuple.page;
        }

        //nothing matched
        return null;
    }

    public static class PageTuple {
        private final PathMatcher matcher;
        private final RenderableWidget page;

        public PageTuple(PathMatcher matcher, RenderableWidget page) {
            this.matcher = matcher;
            this.page = page;
        }
    }
}
