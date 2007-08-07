package com.wideplay.warp.module.ioc;

import com.google.inject.Provider;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 7, 2007
 * Time: 4:40:38 PM
 */
class ResponseProvider implements Provider<HttpServletResponse> {

    public HttpServletResponse get() {
        return IocContextManager.getResponse();
    }
}
