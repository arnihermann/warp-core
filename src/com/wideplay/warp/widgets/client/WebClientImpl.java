package com.wideplay.warp.widgets.client;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WebClientImpl<T> implements WebClient<T>, TransportTypeChainBuilder<T>, WebMethodChainBuilder<T> {
    public WebClientImpl() {
    }

    public WebResponse get() {
        return null;
    }

    public WebResponse get(Object body) {
        return null;
    }

    public WebResponse post() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse post(Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse put() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse put(Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse delete() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse delete(Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse head() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebResponse head(Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TransportTypeChainBuilder<T> request(String url) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TransportTypeChainBuilder<T> request(String url, Map<String, String> headers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebMethodChainBuilder<T> over(Class<Json> transport) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public WebMethodChainBuilder<T> requestPlain(String url) {
        return null;
    }

    public WebMethodChainBuilder<T> requestPlain(String url, Map<String, String> headers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
