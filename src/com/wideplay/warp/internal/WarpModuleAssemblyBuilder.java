package com.wideplay.warp.internal;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wideplay.warp.WarpModule;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.internal.components.ComponentBuilders;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.PageHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;

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
            log.debug("Classes are in: " + classLocation + " and in package: " + packageName);
            url = new URL(classLocation);
        } catch(MalformedURLException e) {
            log.error("could not create classfactory for module", e);
            System.out.println("could not create classfactory for module");
        }

        //load warp user module classes
        List<Class<?>> allClasses = new ClassListBuilder().loadClasses(url, packageName);

        //startup services
        ComponentRegistry componentRegistry = ComponentBuilders.newComponentRegistry();
        internalServicesModule.setComponentRegistry(componentRegistry);

        //build page reflections and assemble them into the module
        Map<String, PageHandler> pages = new LinkedHashMap<String, PageHandler>();
        for (Class<?> pageClass : allClasses) {
            //only build handlers for page classes (skips enums, etc.)
            if (isNonPageClass(pageClass))
                continue;

            //should be built as a custom component?
            if (isComponentClass(pageClass))
                ComponentBuilders.buildAndRegisterComponent(context, componentRegistry, packageName, pageClass);
            else
                PageBuilders.buildAndStorePageHandler(context, componentRegistry, pageClass, packageName, pages);
        }

        //build a guice module out of the loaded pages
        List<PageClassReflection> pageBindings = new ArrayList<PageClassReflection>(pages.size());
        Map<Class<?>, String> pagesURIs = new LinkedHashMap<Class<?>, String>();
        for (String pageURI : pages.keySet()) {

            log.debug("Binding page to provider (in guice) and to URI : " + pageURI);
            PageHandler pageHandler = pages.get(pageURI);
            Class<?> pageClass = pageHandler.getPageClassReflection().getPageClass();

            //register class and URI
            pageBindings.add(pageHandler.getPageClassReflection());
            pagesURIs.put(pageClass, pageURI);
        }


        return configureGuice(pageBindings, pages, pagesURIs);
    }


    
    private WarpModuleAssembly configureGuice(List<PageClassReflection> pageBindings, Map<String, PageHandler> pages, Map<Class<?>, String> pagesURIs) {
        //load user defined guice module here
        Module warpGuiceModule = IocContextManager.newDefaultGuiceModule(pageBindings);
        module.configure(new WarpConfigurerImpl(warpGuiceModule));

        //build an application injector from configured modules
        WarpModuleAssemblyProvider moduleAssemblyProvider = new WarpModuleAssemblyProvider();
        internalServicesModule.setWarpModuleAssemblyProvider(moduleAssemblyProvider);
        Injector injector = Guice.createInjector(internalServicesModule, warpGuiceModule);

        //make the assembly available to the guice injector via a pre-registered provider
        WarpModuleAssembly warpModuleAssembly = new WarpModuleAssembly(pages, injector, pagesURIs);
        moduleAssemblyProvider.setAssembly(warpModuleAssembly);
        
        return warpModuleAssembly;
    }


    

    private boolean isNonPageClass(Class<?> pageClass) {
        return pageClass.isAnnotation() || pageClass.isEnum() || pageClass.isInterface();
    }

    private boolean isComponentClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }


    private class InternalServicesModule extends AbstractModule {
        private WarpModuleAssemblyProvider warpModuleAssemblyProvider;
        private ComponentRegistry componentRegistry;

        protected void configure() {
            bind(WarpModuleAssembly.class).toProvider(warpModuleAssemblyProvider);
            bind(ComponentRegistry.class).toInstance(componentRegistry);
        }

        public void setWarpModuleAssemblyProvider(WarpModuleAssemblyProvider warpModuleAssemblyProvider) {
            this.warpModuleAssemblyProvider = warpModuleAssemblyProvider;
        }

        public void setComponentRegistry(ComponentRegistry componentRegistry) {
            this.componentRegistry = componentRegistry;
        }
    }
}
