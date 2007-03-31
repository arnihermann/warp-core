package com.wideplay.warp;

import com.wideplay.warp.internal.Builders;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.rendering.TemplatingFilter;
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
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class WarpFilter implements Filter {
    private TemplatingFilter templatingFilter;

    private static final String WARP_MODULE = "warp.module";
    private static final String WARP_PACKAGE = "warp.package";
    public static final String CLASS_EXT = ".class";

    public static IocContextManager contextManager = IocContextManager.newServletIocContext();
    private final Log log = LogFactory.getLog(getClass());

    
    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        //set injection context for servlet
        IocContextManager.setContext((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse);

        //dispatch to warp filter
        if (!templatingFilter.doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse))
            //continue down the chain
            filterChain.doFilter(servletRequest, servletResponse);

        //clear injection context for servlet
        IocContextManager.clearContext();
    }


    
    public void init(FilterConfig filterConfig) throws ServletException {
        //build warp module assembly
        String warpModuleName = filterConfig.getServletContext().getInitParameter(WARP_MODULE);
        String modulePackage = filterConfig.getServletContext().getInitParameter(WARP_PACKAGE);

        //try to load module class
        Class<WarpModule> moduleClass = null;
        try {
             moduleClass = (Class<WarpModule>) Class.forName(warpModuleName);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServletException("error could not locate the warp module class: " + warpModuleName, e);
        }


        
        try {
            //guess module name & root dirs from module class package if not specified
            if (null == modulePackage)
                modulePackage = moduleClass.getPackage().getName();
            String moduleRootDir = moduleClass.getResource(moduleClass.getSimpleName() + CLASS_EXT).toString();

            //strip ModuleName.class from module root dir url to get the directory name
            moduleRootDir = moduleRootDir.substring(0, moduleRootDir.length() - (CLASS_EXT.length() + moduleClass.getSimpleName().length()));

            log.info("Using module package: " + modulePackage + " ; server module root dir: " + moduleRootDir);

            //build the warp module pages, components and handlers into an assembly
            WarpModuleAssembly assembly = Builders.buildWarpModuleAssembly(moduleClass, filterConfig.getServletContext(), moduleRootDir, modulePackage);

            //build internal services
            templatingFilter = new TemplatingFilter(assembly, filterConfig.getServletContext());
            
        } catch(Throwable e) {
            e.printStackTrace();
            throw new ServletException("error during WarpFilter init", e);
        }
    }
}
