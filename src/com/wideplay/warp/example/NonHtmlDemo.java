package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Template;
import com.wideplay.warp.annotations.URIMapping;
import com.wideplay.warp.annotations.OnEvent;
import com.wideplay.warp.annotations.Context;
import com.wideplay.warp.annotations.event.PostRender;
import com.google.inject.Inject;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 7:28:07 PM
 *
 * This page object backs a non-html template (a simple flat-text document with embedded
 * mvel expressions).
 *
 * If, like me, you put this template in the classpath, then make sure you configure your
 * build script/IDE to copy .txt (or whatever extension) to WEB-INF/classes
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
@URIMapping("/NonHtmlDemo")
@Template(name = "NonHtmlDemo.txt")
public class NonHtmlDemo {
    private final String message = "Hello from Not-html =)";

    @Inject @Context
    HttpServletResponse response;

    @OnEvent @PostRender
    void setContentType() {
        //override the default content type of text/html
        response.setContentType("text/plain");
    }

    public String getMessage() {
        return message;
    }
}
