package com.wideplay.warp.example;

import com.google.inject.AbstractModule;
import com.wideplay.warp.Warp;
import com.wideplay.warp.WarpModule;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ExampleModule extends AbstractModule implements WarpModule {

    public void configure(Warp warp) {
        //install me as a module (convenience)
        warp.install(this);
    }


    protected void configure() {
        //bind custom impls here as you would in guice
    }
}
