package com.wideplay.warp.util.reflect;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * Created with IntelliJ IDEA.
 * On: 19/02/2007
 *
 * This is a classloader that invokes a specified bean or set of beans
 * in an isolated context.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class UrlInverseClassLoader extends URLClassLoader {
    private ClassLoader parentClassLoader;

    public UrlInverseClassLoader(URL[] urls) {
        super(urls);
    }

    public UrlInverseClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);

        parentClassLoader = parent;
    }

    public UrlInverseClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);

        parentClassLoader = parent;
    }


    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, true);
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class c = findLoadedClass(name);

        if (c == null) {
            //if not try to find & load into "this" classloader
            try {
                c = findClass(name);
            } catch(NoClassDefFoundError ncdfe) {
                //if that doesnt work, delegate to the parent (invert the classloader order)
                getParent().loadClass(name);
            } catch (ClassNotFoundException e) {
                //if that doesnt work, delegate to the parent (invert the classloader order)
                getParent().loadClass(name);
            }
        }

        if (resolve) {
            resolveClass(c);
        }

        return c;
    }
}
