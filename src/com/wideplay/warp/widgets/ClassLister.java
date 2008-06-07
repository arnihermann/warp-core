package com.wideplay.warp.widgets;

import com.google.inject.matcher.Matcher;
import net.jcip.annotations.NotThreadSafe;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class ClassLister {
    private final ServletContext context;

    private static final String PREFIX = "/WEB-INF/classes/";
    private static final String CLASS_SUFFIX = ".class";

    private final Logger log = Logger.getLogger(ClassLister.class.toString());

    public ClassLister(ServletContext context) {
        this.context = context;
    }

    public Set<Class<?>> list(Package pack, Matcher<? super Class<?>> matcher) {
        Set<Class<?>> classes = new HashSet<Class<?>>();

        final String searchIn = String.format("%s%s/", PREFIX,
                pack.getName()
                    .replaceAll("\\.", "/")
        );
        
        list(classes, searchIn, context, matcher);

        return classes;
    }

    private void list(Set<Class<?>> classes, String mainPath, ServletContext context,
                      Matcher<? super Class<?>> matcher) {

        @SuppressWarnings("unchecked")
        final Set<String> resourcePaths = context.getResourcePaths(mainPath);

        if (null == resourcePaths) {
            log.severe(String.format("Could not find any resources from servlet context path at %s. The Servlet Container did not expose any available classes or packages (is WEB-INF/classes empty?)", mainPath));
            throw new IllegalStateException("Could not find any resources from servlet context path");
        }

        for (String path : resourcePaths) {
            if (isClass(path)) {
                Class<?> clazz = toClass(path.substring(PREFIX.length()));

                if (matcher.matches(clazz)) {
                    log.fine(String.format("%s matches criteria %s, adding...", clazz, matcher));
                    classes.add(clazz);
                }
            }
            
            if (isDir(path)) //recurse if is a dir
                list(classes, path, context, matcher);
        }
    }

    private Class<?> toClass(String path) {
        try {
            return Class.forName(path
                    .substring(0, path.length() - CLASS_SUFFIX.length())
                    .replaceAll("/", ".")
            );
        } catch (ClassNotFoundException e) {
            log.severe("A class discovered by the scanner could not be found by the ClassLoader, something very odd has happened with the classloading (see root cause) " + e.toString());
            return null;    //something went wrong
        }
    }

    private static boolean isClass(String path) {
        return path.endsWith(CLASS_SUFFIX);
    }

    private static boolean isDir(String path) {
        return path.substring(PREFIX.length()).endsWith("/");
    }
}
