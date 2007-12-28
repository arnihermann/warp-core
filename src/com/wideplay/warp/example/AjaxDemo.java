package com.wideplay.warp.example;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.guice.GuiceCreator;
import com.wideplay.warp.annotations.Asset;

/**
 * Created with IntelliJ IDEA.
 * On: 21/08/2007
 *
 * This class doubles as a Warp page and a Dwr Remote service. Don't let it confuse you.
 * It is requested once via synchronous request and again when you type in the textbox via
 * Dwr and ajax. Those two invocations are entirely different (the equivalent of 2 separate
 * objects).
 *
 *
 * Observe that the DWR javascript resource is bundled on the classpath, we tell warp to look
 * for it with the @Asset annotation (allowed on any warp-pages). This helps if you want to
 * package js, css, etc., in a jar as a library of warp widgets/pages/dwr services.
 * You are free to register as many @Assets as you like so long as the files are present in the
 * same package.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@RemoteProxy(creator = GuiceCreator.class)
@Asset(uri = "/suggest.js", resource = "suggest.js")    //tell warp to load and serve suggest.js
public class AjaxDemo {

    @RemoteMethod
    public String remoteMethod(String name) {
        return String.format("Hi there, %s!", name);
    }
}
