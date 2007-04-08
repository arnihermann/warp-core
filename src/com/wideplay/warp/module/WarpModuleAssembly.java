package com.wideplay.warp.module;

import com.google.inject.Injector;
import com.wideplay.warp.rendering.PageHandler;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class WarpModuleAssembly {
    private final Map<String, PageHandler> pages;
    private final Map<Class<?>, String> pageURIs;
    private final Injector injector;

    public WarpModuleAssembly(Map<String, PageHandler> pages, Injector injector, Map<Class<?>, String> pageURIs) {
        this.pages = pages;
        this.injector = injector;
        this.pageURIs = pageURIs;
    }

    public PageHandler getPage(String uri) {
        return pages.get(uri);
    }

    public Injector getInjector() {
        return injector;
    }

    /**
     *
     * @param page A Warp Page object POJO
     * @return Returns the registered URI of this page object if it can be found.
     * This method uses devious means, lots of caching and reflecting on the object class
     * to figure out the object's identity. It is a bit of a WIP.
     *
     */
    public String resolvePageURI(Object page) {
        String uri = pageURIs.get(page.getClass());

        //if maybe this is a subclass of a page class?
        if (null == uri)
            uri = resolveFromParents(page.getClass(), page.getClass().getSuperclass());

        return uri;
    }

    //recursively search the super classes until a match is found
    private String resolveFromParents(Class<?> originalClass, Class<?> clazz) {

        //reached the top of the tree, nothing found
        if (Object.class.equals(clazz))
            return null;

        //else see if this super matches
        String pageURI = pageURIs.get(clazz);
        if (null != pageURI) {
            //woohoo! found, cache the original class as a subtype of this
            pageURIs.put(originalClass, pageURI);

            return pageURI;
        }

        //tail recurse
        return resolveFromParents(originalClass, clazz.getSuperclass());
    }


}
