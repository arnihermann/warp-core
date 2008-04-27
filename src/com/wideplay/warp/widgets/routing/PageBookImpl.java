package com.wideplay.warp.widgets.routing;

import com.wideplay.warp.widgets.RenderableWidget;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.Post;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import com.google.inject.Singleton;
import com.google.inject.Injector;
import com.google.inject.Inject;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.annotation.Annotation;

/**
 * contains active uri/widget mappings
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Singleton @ThreadSafe
class PageBookImpl implements PageBook {
    //multimaps TODO refactor to multimap?
    private final Map<String, List<PageTuple>> pages = new HashMap<String, List<PageTuple>>();
    private final List<PageTuple> universalMatchingPages = new ArrayList<PageTuple>();
    private final Map<String, PageTuple> pagesByName = new HashMap<String, PageTuple>();

    private final Injector injector;

    private final Object lock = new Object();

    @Inject
    PageBookImpl(Injector injector) {
        this.injector = injector;
    }

    public void at(String uri, RenderableWidget page, Class<?> clazz) {
        synchronized (lock) {
            final String key = firstPathElement(uri);

            final PageTuple pageTuple = new PageTuple(new PathMatcherChain(uri), page, clazz, injector);

            //store in alias map if necessary
            if (clazz.isAnnotationPresent(EmbedAs.class)) {
                pagesByName.put(clazz.getAnnotation(EmbedAs.class).value(), pageTuple);
            }

            //is universal? (i.e. first element is a variable)
            if (isFirstElementVariable(key))
                universalMatchingPages.add(pageTuple);
            else {
                multiput(pages, key, pageTuple);
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

    public Page get(String uri) {
        final String key = firstPathElement(uri);

        List<PageTuple> tuple = pages.get(key);

        //first try static first piece
        if (null != tuple) {

            //first try static first piece
            for (PageTuple pageTuple : tuple) {
                if (pageTuple.matcher.matches(uri))
                    return pageTuple;
            }
        }

        //now try dynamic first piece (how can we make this faster?)
        for (PageTuple pageTuple : universalMatchingPages) {
            if (pageTuple.matcher.matches(uri))
                return pageTuple;
        }

        //nothing matched
        return null;
    }

    public Page forName(String name) {
        return pagesByName.get(name);
    }

    public static class PageTuple implements Page {
        private final PathMatcher matcher;
        private final RenderableWidget pageWidget;
        private final Class<?> clazz;
        private final Injector injector;

        private final Method get;
        private final Method post;

        public PageTuple(PathMatcher matcher, RenderableWidget pageWidget, Class<?> clazz, Injector injector) {
            this.matcher = matcher;
            this.pageWidget = pageWidget;
            this.clazz = clazz;
            this.injector = injector;

            this.get = reflect(Get.class);
            this.post = reflect(Post.class);
        }

        private Method reflect(Class<? extends Annotation> annotation) {
            //first search class's methods only

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh
                    return method;
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh
                    return method;
                }
            }

            return null;
        }

        public RenderableWidget widget() {
            return pageWidget;
        }

        public Object instantiate() {
            return injector.getInstance(clazz);
        }

        public void doGet(Object page) {
            call(page, get);
        }

        public void doPost(Object page) {
            call(page, post);
        }

        private void call(Object page, final Method method) {
            if (null != method)
                try {
                    method.invoke(page);
                } catch (IllegalAccessException e) {
                    throw new EventDispatchException("Could not access event method: " + method, e);
                } catch (InvocationTargetException e) {
                    throw new EventDispatchException("Event method threw an exception: " + method, e);
                }
        }

    }
}
