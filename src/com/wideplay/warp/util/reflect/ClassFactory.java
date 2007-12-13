package com.wideplay.warp.util.reflect;


import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 19/02/2007
 *
 * This is a factory that builds and invokes beans/bean deps in isolated
 * classloaders.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class ClassFactory {
    private final ClassLoader classLoader;
    private final URL[] urls;
    private final String pkg;

    private ClassFactory(URL[] urls, ClassLoader classLoader) {
        this.urls = urls;
        this.classLoader = classLoader;
        this.pkg = "";
    }

    private ClassFactory(String pkg, URL[] urls, URLClassLoader classLoader) {
        this.pkg = pkg + ".";
        this.urls = urls;
        this.classLoader = classLoader;
    }

    public Object newBean(String className, Class[] argTypes, Object[] args)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {

        Class clazz = loadClass(className);
        Constructor constructor = clazz.getConstructor(argTypes);

        if (!constructor.isAccessible())
            constructor.setAccessible(true);

        //instantiate bean
        return constructor.newInstance(args);
    }


    /**
     * Invokes the application in this jar file given the name of the
     * bean class and an array of arguments. The class must define a
     * method which takes an array of arguments as specified by the
     * arguments passed to this method and is of return type "void".
     *
     * @param className the name of the bean class
     * @param args the arguments for the bean
     * @param methodName the name of the method on the bean (does not have to be a getter/setter)
     * @param argTypes The types of each argument (must correspond to args)
     *
     * @exception ClassNotFoundException if the specified class could not
     *            be found
     * @exception NoSuchMethodException if the specified class does not
     *            contain a "main" method
     * @exception java.lang.reflect.InvocationTargetException if the application raised an
     *            exception
     * @exception IllegalAccessException if the specified method was not accessible
     */
    public Object invokeBean(String className, String methodName, Class[] argTypes, Object[] args)
            throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class clazz = loadClass(className);
        Method m = clazz.getMethod(methodName, argTypes);

        if (!m.isAccessible())
            m.setAccessible(true);

        //instantiate bean
        Object bean = ReflectUtils.instantiate(clazz);

        //invoke method
        return m.invoke(bean, args);
    }


    public <T> T makeBeanAndInvoke(String className, String methodName, Class[] argTypes, Object[] args, Class<T> returnType) {
        try {
            return (T) invokeBean(className, methodName, argTypes, args);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot invoke bean method provided", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot invoke bean method provided", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot invoke bean method provided", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot invoke bean method provided", e);
        }
    }

    public <T> T makeBean(String className, Class[] argTypes, Object[] args, Class<T> returnType) {
        try {
            return (T) newBean(className, argTypes, args);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("cannot make bean provided", e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("cannot make bean provided", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("cannot make bean provided", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("cannot make bean provided", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("cannot make bean provided", e);
        }
    }

    public <T> T makeBean(String className, Class<T> returnType) {
        return makeBean(className, null, null, returnType);
    }

    public Object makeBean(String className) {
        return makeBean(className, null, null, Object.class);
    }

    public Class<?> loadClass(String name) {
        name = pkg + name;
        
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            System.out.println("could not load class: " + name);
            throw new IllegalArgumentException("cannot find class", e);
        }
    }

    public List<Class<?>> loadAllClasses() {
        List<Class<?>> classes = new LinkedList<Class<?>>();

        //recursively iterate package tree and load classes
        for (URL url : urls) {
            List<String> classNames = ClassNameUtils.listRecursive(new File(url.getFile()), ClassNameUtils.CLASS_FILE_FILTER);

            //ensure class is loaded
            for (String className : classNames)
                classes.add(loadClass(className));
        }

        return classes;
    }

    public List<Class<?>> loadAllClasses(String packageName) {
        List<Class<?>> classes = new LinkedList<Class<?>>();

        //recursively iterate package tree and load classes
        for (URL url : urls) {
            List<String> classNames = ClassNameUtils.listRecursive(new File(url.getFile()), ClassNameUtils.CLASS_FILE_FILTER);

            //ensure class is loaded
            for (String className : classNames)
                classes.add(loadClass(className));
        }

        return classes;
    }


    //factories -------------
    public static ClassFactory newUrlPackageClassFactory(String pkg, URL...urls) {
        return new ClassFactory(pkg, urls, URLClassLoader.newInstance(urls));
    }

    public static ClassFactory newUrlClassFactory(URL...urls) {
        return new ClassFactory(urls, URLClassLoader.newInstance(urls));
    }

    public static ClassFactory newChildUrlClassFactory(ClassLoader parent, URL...urls) {
        return new ClassFactory(urls, URLClassLoader.newInstance(urls, parent));
    }

}
