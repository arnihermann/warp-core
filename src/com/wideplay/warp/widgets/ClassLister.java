package com.wideplay.warp.widgets;

import com.google.inject.Inject;
import com.google.inject.matcher.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class ClassLister {
    private final ServletContext context;
    
    private static final String PREFIX = "/WEB-INF/classes/";
    private static final String CLASS_SUFFIX = ".class";

    private final Logger log = LoggerFactory.getLogger(ClassLister.class);

    @Inject
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

        for (String path : resourcePaths) {
            if (isClass(path)) {
                Class<?> clazz = toClass(path.substring(PREFIX.length()));

                if (matcher.matches(clazz)) {
                    log.trace(String.format("%s matches criteria %s, adding...", clazz, matcher));
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
            log.error("A class discovered by the scanner could not be found by the ClassLoader, something very odd has happened with the classloading (see root cause)", e);
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
