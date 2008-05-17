package com.wideplay.warp.widgets.routing;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.wideplay.warp.widgets.Get;
import com.wideplay.warp.widgets.On;
import com.wideplay.warp.widgets.Post;
import com.wideplay.warp.widgets.RenderableWidget;
import com.wideplay.warp.widgets.rendering.EmbedAs;
import net.jcip.annotations.ThreadSafe;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
                pagesByName.put(clazz.getAnnotation(EmbedAs.class).value().toLowerCase(), pageTuple);
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
        return key.length() > 0 && ':' == key.charAt(0);
    }

    String firstPathElement(String uri) {
        String shortUri = uri.substring(1);

        final int index = shortUri.indexOf("/");

        return (index >= 0) ? shortUri.substring(0, index) : shortUri;
    }

    @Nullable
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


    @On("") //the default on (hacky!!)
    public static class PageTuple implements Page {
        private final PathMatcher matcher;
        private final RenderableWidget pageWidget;
        private final Class<?> clazz;
        private final Injector injector;

        private final Map<String, MethodTuple> get;
        private final Map<String, MethodTuple> post;

        //dispatcher switch
        private final On on;

        public PageTuple(PathMatcher matcher, RenderableWidget pageWidget, Class<?> clazz, Injector injector) {
            this.matcher = matcher;
            this.pageWidget = pageWidget;
            this.clazz = clazz;
            this.injector = injector;

            this.on = reflectOn(clazz);
            this.get = reflectGet();
            this.post = reflectPost();
        }

        //the @On request parameter-based event dispatcher
        private On reflectOn(Class<?> clazz) {
            final On on = clazz.getAnnotation(On.class);
            if (null != on)
                return on;
            else
                return PageTuple.class.getAnnotation(On.class);
        }


        /**
         * Returns a map of @Get.value() to @Get-marked methods
         */
        @SuppressWarnings({"JavaDoc"})
        private Map<String, MethodTuple> reflectGet() {
            final Map<String, MethodTuple> map = new HashMap<String, MethodTuple>();

            //first search class's methods only
            final Class<Get> get = Get.class;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(get)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh

                    //remember default value is empty string
                    map.put(method.getAnnotation(get).value(), new MethodTuple(method));
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(get)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh

                    //remember default value is empty string
                    map.put(method.getAnnotation(get).value(), new MethodTuple(method));
                }
            }

            return map;
        }


        /**
         * Returns a map of @Post.value() to @Post-marked methods
         */
        @SuppressWarnings({"JavaDoc"})
        private Map<String, MethodTuple> reflectPost() {
            final Map<String, MethodTuple> map = new HashMap<String, MethodTuple>();

            //first search class's methods only
            final Class<Post> get = Post.class;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(get)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh

                    //remember default value is empty string
                    map.put(method.getAnnotation(get).value(), new MethodTuple(method));
                }
            }

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(get)) {
                    if (!method.isAccessible())
                        method.setAccessible(true); //ugh

                    //remember default value is empty string
                    map.put(method.getAnnotation(get).value(), new MethodTuple(method));
                }
            }

            return map;
        }

        public RenderableWidget widget() {
            return pageWidget;
        }

        public Object instantiate() {
            return injector.getInstance(clazz);
        }

        public Object doGet(Object page, String pathInfo, Map<String, String[]> params) {
            //nothing to fire
            if (get.isEmpty())
                return null;

            final Map<String, String> map = matcher.findMatches(pathInfo);

            //find method to dispatch to
            final String[] events = params.get(on.value());

            if (null != events)
                for (String event : events) {
                    MethodTuple methodTuple = get.get(event);

                    //no event handler registered for this value (so fire to the default)
                    if (null == methodTuple)
                        methodTuple = get.get("");

                    //or fire event handler(s)
                    Object redirect = methodTuple.call(page, map);

                    //redirects interrupt the event dispatch sequence
                    if (null != redirect)
                        return redirect;
                }
            else
                //fire default handler
                return get.get("").call(page, map);

            //no redirects, render normally
            return null;
        }

        public Object doPost(Object page, String pathInfo, Map<String, String[]> params) {
            //nothing to fire
            if (post.isEmpty())
                return null;

            final Map<String, String> map = matcher.findMatches(pathInfo);

            //find method ot dispatch to
            final String[] events = params.get(on.value());

            if (null != events)
                for (String event : events) {
                    MethodTuple methodTuple = post.get(event);

                    //no event handler registered (so fire to the default)
                    if (null == methodTuple)
                        methodTuple = post.get("");

                    //or fire event handler(s)
                    Object redirect = methodTuple.call(page, map);

                    //redirects interrupt the event dispatch sequence
                    if (null != redirect)
                        return redirect;
                }
            else
                //fire default handler
                return post.get("").call(page, map);

            //no redirects, render normally
            return null;  
        }
    }
    

    private static class MethodTuple {
        private final Method method;
        private final List<String> args;

        private MethodTuple(Method method) {
            this.method = method;
            this.args = reflect(method);
        }

        private List<String> reflect(Method method) {
            final Annotation[][] annotationsGrid = method.getParameterAnnotations();
            if (null == annotationsGrid)
                return Collections.emptyList();

            List<String> args = new ArrayList<String>();
            for (Annotation[] annotations : annotationsGrid) {
                boolean namedFound = false;
                for (Annotation annotation : annotations) {
                    if (Named.class.isInstance(annotation)) {
                        Named named = (Named) annotation;

                        args.add(named.value());
                        namedFound = true;

                        break;
                    }
                }

                if (!namedFound)
                    throw new InvalidEventHandlerException("Encountered an argument not annotated with @Named in event handler method: " + method);
            }

            return Collections.unmodifiableList(args);
        }

        public Object call(Object page, Map<String, String> map) {
            List<String> arguments = new ArrayList<String>();
            for (String argName : args) {
                arguments.add(map.get(argName));
            }

            return call(page, method, arguments.toArray());
        }

        private static Object call(Object page, final Method method, Object[] args) {
            try {
                return method.invoke(page, args);
            } catch (IllegalAccessException e) {
                throw new EventDispatchException("Could not access event method: " + method, e);
            } catch (InvocationTargetException e) {
                throw new EventDispatchException("Event method threw an exception: " + method, e);
            }
        }
    }
}
