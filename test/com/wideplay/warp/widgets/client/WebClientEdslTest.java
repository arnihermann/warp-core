package com.wideplay.warp.widgets.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WebClientEdslTest {

//    @Test
    public final void edslForBinding() {
        final Injector injector = Guice.createInjector();

        WebClient<Object> client = new WebClientFactoryImpl(new Provider<WebClientBuilder>() {
            public WebClientBuilder get() {
                return new WebClientBuilder(injector);
            }
        })
                .clientOf("http://localhost:4040/warp/")

                .transporting(Object.class)
                .using(Json.class);

        assert null != client;

//        client.get()
//                .to(Object.class).using(Json.class);

        System.out.println(client.get().toString());
    }
}
