package com.wideplay.warp.module.ioc;

import com.google.inject.Provider;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 7, 2007
 * Time: 4:39:15 PM
 */
class RequestProvider implements Provider<HttpServletRequest> {

    public HttpServletRequest get() {
        return IocContextManager.getRequest();
    }
}
