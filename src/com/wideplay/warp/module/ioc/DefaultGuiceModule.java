package com.wideplay.warp.module.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;
import com.wideplay.warp.annotations.Page;
import com.wideplay.warp.internal.Builders;
import com.wideplay.warp.module.StateManager;
import com.wideplay.warp.module.componentry.ClassReflectionCache;
import com.wideplay.warp.module.pages.InjectPageProvider;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.RequestBinder;
import com.wideplay.warp.util.Cube;
import com.wideplay.warp.util.HashCube;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class DefaultGuiceModule extends AbstractModule {
    private Map<Class<?>, InjectPageProvider> pagesAndProviders = new LinkedHashMap<Class<?>, InjectPageProvider>();

    @SuppressWarnings("unchecked")
    protected void configure() {
        //bind warp scopes
        bindScope(SessionScoped.class, WarpScopes.SESSION);
        bindScope(RequestScoped.class, WarpScopes.REQUEST);

        //bind request & response providers
        bind(HttpServletRequest.class).toProvider(RequestProvider.class);
        bind(HttpServletResponse.class).toProvider(ResponseProvider.class);

        //bind state manager and its deps
        bind(Cube.class).annotatedWith(SessionWide.class).to(HashCube.class).in(SessionScoped.class);
        bind(StateManager.class).to(StateManagerImpl.class).in(Singleton.class);

        bind(ClassReflectionCache.class).toInstance(Builders.newClassReflectionCache());

        //bind reserved parameters
        bind(new TypeLiteral<Set<String>>() {})
                .annotatedWith(ReservedParameters.class)
                .toInstance(Builders.getReservedParameterNames());

        //bind request binder that uses ognl to bind request parameters to a page
        bind(RequestBinder.class).to(OgnlRequestBinder.class);

        //bind pages to our InjectPageProvider providers (ones that only inject managed props)
        for (Class<?> clazz : pagesAndProviders.keySet())
            bind(clazz).annotatedWith(Page.class).toProvider(pagesAndProviders.get(clazz));
    }

    //invoked by parent to store class proxy classes
    public <T> void bindPage(PageClassReflection reflection) {
        pagesAndProviders.put(reflection.getPageClass(), InjectPageProvider.provide(reflection));
    }
}
