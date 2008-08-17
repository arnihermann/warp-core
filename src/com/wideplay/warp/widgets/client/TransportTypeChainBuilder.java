package com.wideplay.warp.widgets.client;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface TransportTypeChainBuilder<T> {

    WebMethodChainBuilder<T> over(Class<Json> transport);
}
