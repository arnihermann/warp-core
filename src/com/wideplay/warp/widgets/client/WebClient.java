package com.wideplay.warp.widgets.client;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface WebClient<T> {

    TransportTypeChainBuilder<T> request(String url);

    TransportTypeChainBuilder<T> request(String url, Map<String, String> headers);

    WebMethodChainBuilder<T> requestPlain(String url);

    WebMethodChainBuilder<T> requestPlain(String url, Map<String, String> headers);
}
