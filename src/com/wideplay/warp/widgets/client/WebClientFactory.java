package com.wideplay.warp.widgets.client;

import com.google.inject.ImplementedBy;
import com.google.inject.Key;

import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ImplementedBy(WebClientFactoryImpl.class)
public interface WebClientFactory {

    public <T> TransportBuilder<T> clientOf(String url);

    public <T> TransportBuilder<T> clientOf(String url, Map<String, String> headers);

    public static interface TransportBuilder<T> {
        UsingBuilder<T> transporting(Class<T> clazz);
    }

    public static interface UsingBuilder<T> {
        WebClient<T> using(Class<? extends Transport> clazz);
        WebClient<T> using(Key<? extends Transport> key);
    }
}
