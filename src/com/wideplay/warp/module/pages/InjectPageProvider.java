package com.wideplay.warp.module.pages;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.cglib.proxy.Proxy;
import com.google.inject.cglib.proxy.Enhancer;
import com.google.inject.cglib.proxy.LazyLoader;
import com.wideplay.warp.module.StateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 21/03/2007
 * Time: 13:59:57
 * <p/>
 *
 * Provides pages that are injected with the @Page annotation (sets only managed properties and constants).
 *
 * Anything marked @Page *within* the object will be injected with a proxy (to prevent eager-injecting a giant
 * page-chain of every referenced @Page, every request) that knows how to lazy-inject itself.
 *
 * This class must do the injection itself (mirroring guice capabilities) because guice is viral and there
 * is no way to selectively not-inject certain properties.
 *
 * @author dprasanna
 * @since 1.0
 */
public class InjectPageProvider<T> implements Provider<T> {
    private final PageClassReflection reflection;
    @Inject private Injector injector;
    @Inject private StateManager stateManager;

    private final Log log = LogFactory.getLog(InjectPageProvider.class);

    public InjectPageProvider(PageClassReflection reflection) {
        this.reflection = reflection;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        if (log.isTraceEnabled())
            log.trace(String.format("Obtaining proxied page for @Page: %s", reflection.getPageClass().getSimpleName()));

        //create a proxy that knows how to lazy-initialize itself (this any @Page-injected objects do not cascade injection virally)
        return (T) Enhancer.create(reflection.getPageClass(), new LazyLoader() {

            public Object loadObject() throws Exception {
                if (log.isTraceEnabled())
                    log.trace(String.format("Lazy-instantiating proxied page for @Page: %s", reflection.getPageClass().getSimpleName()));
                return injector.getInstance(reflection.getPageClass());
            }
        });
    }

    public static <T> InjectPageProvider<T> provide(PageClassReflection reflection) {
        return new InjectPageProvider<T>(reflection);
    }
}
