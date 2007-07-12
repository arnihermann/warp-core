package com.wideplay.warp.rendering;

import com.google.inject.Injector;
import com.wideplay.warp.module.pages.PageClassReflection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:10:11 PM
 *
 * @author Dhanji R. Prasanna
 */
public interface PageHandler {

    Object handleRequest(HttpServletRequest request, HttpServletResponse response, Injector injector,
                               Object page);

    PageClassReflection getPageClassReflection();

    ComponentHandler getRootComponentHandler();
}
