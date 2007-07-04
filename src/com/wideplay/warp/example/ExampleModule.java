package com.wideplay.warp.example;

import com.google.inject.AbstractModule;
import com.wideplay.warp.Warp;
import com.wideplay.warp.WarpModule;
import com.wideplay.warp.StartupListener;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ExampleModule extends AbstractModule implements WarpModule, StartupListener {

    public void configure(Warp warp) {
        //install me as a module (convenience)
        warp.install(this);

        //install me as the startup listener (another hacky convenience -- note that some methods in this class will be called prior to injection!)
        //this is not a good example to follow in practice =). Ideally you would create your own startup listener impl(s)
        warp.addStartupListener(ExampleModule.class);
    }


    protected void configure() {
        //bind custom impls here as you would in guice
        //....
    }


    public void onStartup() {
        System.out.println("started up baby!");
    }
}
