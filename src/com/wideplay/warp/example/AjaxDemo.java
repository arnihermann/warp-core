package com.wideplay.warp.example;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.guice.GuiceCreator;

/**
 * Created with IntelliJ IDEA.
 * On: 21/08/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@RemoteProxy(creator = GuiceCreator.class)
public class AjaxDemo {

    @RemoteMethod
    public String remoteMethod(String name) {
        return String.format("Hi there, %s!", name);
    }
}
