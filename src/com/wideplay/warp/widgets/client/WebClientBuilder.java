package com.wideplay.warp.widgets.client;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import net.jcip.annotations.NotThreadSafe;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class WebClientBuilder<T> implements WebClientFactory.TransportBuilder<T>,
        WebClientFactory.UsingBuilder<T> {

    //pass around anti-pattern =(
    private final Injector injector;

    private String url;
    private Map<String, String> headers;
    private Class<T> transporting;

    @Inject
    public WebClientBuilder(Injector injector) {
        this.injector = injector;
    }

    public WebClientFactory.UsingBuilder<T> transporting(Class<T> clazz) {
        this.transporting = clazz;

        return this;
    }

    public WebClient<T> using(Class<? extends Transport> clazz) {
        return new WebClientImpl<T>(injector, url, headers, transporting, Key.get(clazz));
    }

    public WebClient<T> using(Key<? extends Transport> key) {
        return new WebClientImpl<T>(injector, url, headers, transporting, key);
    }

    public WebClientFactory.TransportBuilder<T> clientOf(String url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;

        return this;
    }
}
