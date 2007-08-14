package com.wideplay.warp.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.WarpModule;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.internal.componentry.ComponentBuilders;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.WarpConfigurationException;
import com.wideplay.warp.module.WarpConfiguration;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.PageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class WarpModuleAssemblyBuilder {
    private final WarpModule module;
    private final ServletContext context;
    private final InternalServicesModule internalServicesModule = new InternalServicesModule();

    private final Log log = LogFactory.getLog(getClass());

    public WarpModuleAssemblyBuilder(WarpModule module, ServletContext context) {
        this.module = module;
        this.context = context;
    }

    public WarpModuleAssembly build(String classLocation, String packageName) {

        //discover page classes location
        URL url = null;
        try {
            if (log.isDebugEnabled())
                log.debug(String.format("Classes are at: %s and in package: %s", classLocation, packageName));
            url = new URL(classLocation);
        } catch(MalformedURLException e) {
            log.error("could not create classfactory for module", e);
            throw new WarpConfigurationException("could not create classfactory for module");
        }

        //load warp user module classes
        List<Class<?>> allClasses = new ClassListBuilder().loadClasses(url, packageName);

        //startup services
        ComponentRegistry componentRegistry = ComponentBuilders.newComponentRegistry();
        internalServicesModule.setComponentRegistry(componentRegistry);

        //build page reflections and assemble them into the module
        Map<String, PageHandler> pages = new LinkedHashMap<String, PageHandler>();
        Map<String, Object> pagesByTemplate = new HashMap<String, Object>();
        Set<Class<?>> componentClasses = new LinkedHashSet<Class<?>>();
        
        buildPageAndComponentHandlers(allClasses, componentRegistry, packageName, pages, componentClasses, pagesByTemplate);

        //build a guice module out of the loaded pages
        List<PageClassReflection> pageBindings = new ArrayList<PageClassReflection>(pages.size());
        Map<Class<?>, String> pagesURIs = new LinkedHashMap<Class<?>, String>();

        bindPages(pages, pageBindings, pagesURIs);

        WarpModuleAssembly warpModuleAssembly = configureGuice(pageBindings, pages, pagesURIs, pagesByTemplate);
        warpModuleAssembly.hidePages(componentClasses);

        return warpModuleAssembly;
    }

    private void bindPages(Map<String, PageHandler> pages, List<PageClassReflection> pageBindings, Map<Class<?>, String> pagesURIs) {
        for (String pageURI : pages.keySet()) {

            if (log.isDebugEnabled())
                log.debug(String.format("Binding page to provider (in guice) and to URI : %s", pageURI));

            PageHandler pageHandler = pages.get(pageURI);
            Class<?> pageClass = pageHandler.getPageClassReflection().getPageClass();

            //register class and URI
            pageBindings.add(pageHandler.getPageClassReflection());
            pagesURIs.put(pageClass, pageURI);
        }
    }

    private void buildPageAndComponentHandlers(List<Class<?>> allClasses, ComponentRegistry componentRegistry, String packageName,
                                               Map<String, PageHandler> pages, Set<Class<?>> componentClasses,
                                               Map<String, Object> pagesByTemplate) {
        for (Class<?> pageClass : allClasses) {
            //only build handlers for page classes (skips enums, etc.)
            if (isNonClass(pageClass))
                continue;

            //also build as page if necessary
            if (shouldBuildAsPage(pageClass))
                PageBuilders.buildAndStorePageHandler(context, componentRegistry, pageClass, packageName, pages, pagesByTemplate);

            //should be built as a custom component?
            if (shouldBuildAsComponent(pageClass)) {
                ComponentBuilders.buildAndRegisterComponent(context, componentRegistry, pageClass, packageName, pages);
                componentClasses.add(pageClass);
            }
        }
    }


    private WarpModuleAssembly configureGuice(List<PageClassReflection> pageBindings, Map<String, PageHandler> pages, Map<Class<?>, String> pagesURIs,
                                              Map<String, Object> pagesByTemplate) {

        //setup custom special-case provider for the assembly itself
        WarpModuleAssemblyProvider moduleAssemblyProvider = new WarpModuleAssemblyProvider();
        internalServicesModule.setWarpModuleAssemblyProvider(moduleAssemblyProvider);

        //configure various guice modules
        Module warpGuiceModule = IocContextManager.newDefaultGuiceModule(pageBindings);
        WarpConfigurerImpl warpConfigurer = new WarpConfigurerImpl(internalServicesModule, warpGuiceModule);
        module.configure(warpConfigurer);

        //make the configuration available to guice injector
        internalServicesModule.setWarpConfigurationProvider(new WarpConfigurationProvider(warpConfigurer));


        //build an application-wide injector from configured modules
        Injector injector = Guice.createInjector(warpConfigurer
                .getGuiceModules()   //pass in modules as an iterable
        );

        //make the assembly available to the guice injector via a pre-registered provider
        WarpModuleAssembly warpModuleAssembly = new WarpModuleAssembly(pages, injector, pagesURIs,
                warpConfigurer.getStartupListeners(), pagesByTemplate);
        moduleAssemblyProvider.setAssembly(warpModuleAssembly);


        
        return warpModuleAssembly;
    }


    

    private boolean isNonClass(Class<?> pageClass) {
        return pageClass.isAnnotation() || pageClass.isEnum() || pageClass.isInterface();
    }

    private boolean shouldBuildAsComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    private boolean shouldBuildAsPage(Class<?> clazz) {
        return !(Renderable.class.isAssignableFrom(clazz));
    }

    private class InternalServicesModule extends AbstractModule {
        private WarpModuleAssemblyProvider warpModuleAssemblyProvider;
        private WarpConfigurationProvider warpConfigurationProvider;
        private ComponentRegistry componentRegistry;

        protected void configure() {
            bind(WarpModuleAssembly.class).toProvider(warpModuleAssemblyProvider);
            bind(ComponentRegistry.class).toInstance(componentRegistry);
            bind(WarpConfiguration.class).toProvider(warpConfigurationProvider);
        }

        public void setWarpModuleAssemblyProvider(WarpModuleAssemblyProvider warpModuleAssemblyProvider) {
            this.warpModuleAssemblyProvider = warpModuleAssemblyProvider;
        }

        public void setComponentRegistry(ComponentRegistry componentRegistry) {
            this.componentRegistry = componentRegistry;
        }

        public void setWarpConfigurationProvider(WarpConfigurationProvider warpConfigurationProvider) {
            this.warpConfigurationProvider = warpConfigurationProvider;
        }
    }
}
