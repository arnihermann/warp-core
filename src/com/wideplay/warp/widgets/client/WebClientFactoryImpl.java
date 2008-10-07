package com.wideplay.warp.widgets.client;

import com.google.inject.Inject;
import com.google.inject.Provider;
import net.jcip.annotations.Immutable;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
*/
@Immutable
class WebClientFactoryImpl implements WebClientFactory {
    private final Provider<WebClientBuilder> builder;

    @Inject
    public WebClientFactoryImpl(Provider<WebClientBuilder> builder) {
        this.builder = builder;
    }

    @SuppressWarnings("unchecked")  //ugh
    public <T> TransportBuilder<T> clientOf(String url) {
        return builder.get().clientOf(url, null);
    }

    @SuppressWarnings("unchecked")  //ugh
    public <T> TransportBuilder<T> clientOf(String url, Map<String, String> headers) {
        return builder.get().clientOf(url, headers);
    }
}
