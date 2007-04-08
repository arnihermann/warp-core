package com.wideplay.warp.module.ioc;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.wideplay.warp.module.pages.PageClassReflection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 23/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class IocContextManager {
    static final ThreadLocal<Context> localContext = new ThreadLocal<Context>();

    private IocContextManager() {
    }

    public static void setContext(HttpServletRequest request, HttpServletResponse response) {
        localContext.set(new Context(request, response));
    }

    public static void clearContext() {
        localContext.remove();
    }

    static HttpServletRequest getRequest() {
        Context context = localContext.get();
        if (context == null) {
          throw new NotScopeableException("Cannot access scoped object. It appears we"
              + " are not currently inside an HTTP Servlet request");
        }

        return context.getRequest();
    }


    static class Context {
        final HttpServletRequest request;
        final HttpServletResponse response;

        Context(HttpServletRequest request, HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

        HttpServletRequest getRequest() {
            return request;
        }

        HttpServletResponse getResponse() {
            return response;
        }
    }

    static void throwNotScopeableException(String message) {
        throw new NotScopeableException(message);
    }
    
    public static Object constructorInject(Class<?> pageClass, Constructor constructor, List<Key<?>> constructorArgs, Injector injector) {
        return ObjectInjector.constructorInject(pageClass, constructor, constructorArgs, injector);
    }

    public static Module newDefaultGuiceModule(List<PageClassReflection> pageBindings) {
        DefaultGuiceModule module = new DefaultGuiceModule();
        for (PageClassReflection reflection : pageBindings)
            module.bindPage(reflection);

        return module;
    }

    public static IocContextManager newServletIocContext() {
        return new IocContextManager();
    }
}
