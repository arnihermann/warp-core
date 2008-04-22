package com.wideplay.warp;

import com.wideplay.warp.internal.Builders;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.rendering.Templater;
import com.wideplay.warp.util.TextTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class WarpFilter implements Filter {
    private volatile Templater templater;
    private volatile WarpModuleAssembly assembly;

    private volatile ServletContext servletContext;

    private static final String WARP_MODULE = "warp.module";
    private static final String WARP_PACKAGE = "warp.package";
    public static final String CLASS_EXT = ".class";

    public static IocContextManager contextManager = IocContextManager.newServletIocContext();
    private final Log log = LogFactory.getLog(getClass());

    
    public void destroy() {
        assembly.fireShutdownEvents();
//        dwrWarpAdapter.shutdown(servletContext);

        assembly = null;
        templater = null;
        servletContext = null;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        //prep request for dispatch
        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        final String contextualUri = TextTools.extractContextualUri(httpServletRequest);

        //set injection context for servlet
        IocContextManager.setContext(httpServletRequest, (HttpServletResponse)servletResponse);
        try {

            //is a dwr dispatch (intercept for ajax requests)
            if (true) {
//                dwrWarpAdapter.service(contextualUri, httpServletRequest, servletResponse);

            } else if (!templater.process(httpServletRequest, (HttpServletResponse)servletResponse))                //dispatch to warp filter

                //continue down the chain
                filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            //clear injection context for servlet chain
            IocContextManager.clearContext();
        }
    }


    
    public void init(FilterConfig filterConfig) throws ServletException {
        //build warp module assembly
        String warpModuleName = filterConfig.getServletContext().getInitParameter(WARP_MODULE);
        String modulePackage = filterConfig.getServletContext().getInitParameter(WARP_PACKAGE);

        //try to load module class
        Class<WarpModule> moduleClass = loadModuleClass(warpModuleName);


        String moduleRootDir;
        try {
            //guess module name & root dirs from module class package if not specified
            if (null == modulePackage)
                modulePackage = moduleClass.getPackage().getName();
            moduleRootDir = moduleClass.getResource(String.format("%s.class", moduleClass.getSimpleName())).toString();

            //strip ModuleName.class from module root dir url to getValue the directory name
            moduleRootDir = TextTools.extractModuleDirFromFqn(moduleRootDir, moduleClass);

            log.info(String.format("Using module package: %s ; server module root dir: %s", modulePackage, moduleRootDir));


        } catch (Exception e) {
            log.fatal(e);
            throw new ServletException("error while discovering module location(s)", e);
        }

        try {
            //build the warp module pages, components and handlers into an assembly
            assembly = Builders.buildWarpModuleAssembly(moduleClass, filterConfig.getServletContext(), moduleRootDir, modulePackage);

            //build internal services
            templater = assembly.getInjector().getInstance(Templater.class);

            //start dwr services
//            dwrWarpAdapter.start(filterConfig.getServletContext(), assembly.getInjector());

            //initialize user services
            assembly.fireStartupEvents();

        } catch(Exception e) {
            log.fatal(e);
            throw new ServletException("error during WarpFilter init", e);
        }

        //store servlet context config
        this.servletContext = filterConfig.getServletContext();
    }

    @SuppressWarnings("unchecked")
    private Class<WarpModule> loadModuleClass(String warpModuleName) throws ServletException {
        try {
            return (Class<WarpModule>) Class.forName(warpModuleName);
        } catch (Exception e) {
            log.fatal("Warp startup failed, could not locate the warp module class", e);
            throw new ServletException("error could not locate the warp module class: " + warpModuleName, e);
        }
    }
}
