package com.wideplay.warp.widgets.client;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface WebMethodChainBuilder<T> {
    WebResponse get();
    WebResponse get(T o);
    WebResponse post();
    WebResponse post(T o);
    WebResponse put();
    WebResponse put(T o);
    WebResponse delete();
    WebResponse delete(T o);
    WebResponse head();
    WebResponse head(T o);

}
