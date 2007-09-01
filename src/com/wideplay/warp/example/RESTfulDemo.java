package com.wideplay.warp.example;

import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.URIMapping;
import com.wideplay.warp.annotations.event.PreRender;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@URIMapping("/movie/{name}")
public class RESTfulDemo {
    private String movie;

    @OnEvent @PreRender public void onLoadPage(String name) {
        this.movie = name;
    }

    public String getMovie() {
        return movie;
    }
}
