package com.wideplay.warp.widgets.client;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class RestClientTest {

//    @Test
    public final void edslForRequests() {
        WebResponse response = new WebClientImpl<Object>()

                .request("http://blah.com", new HashMap<String, String>())
                .over(Json.class)
                .get(new Object());

        assert null != response;

        response = new WebClientImpl()

                .requestPlain("http://blah.com")
                .post();


        String entity = response.from(Json.class).to(String.class);

        final Map<String,String> headers = response.getHeaders();
    }
}
