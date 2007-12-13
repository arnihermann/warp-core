package com.wideplay.warp.example;

import com.google.inject.Key;
import com.google.inject.Singleton;
import com.wideplay.warp.StartupListener;
import com.wideplay.warp.Warp;
import com.wideplay.warp.WarpModule;
import com.wideplay.warp.rendering.templating.HtmlElement;
import com.wideplay.warp.rendering.templating.HtmlElementFilter;
import org.directwebremoting.guice.AbstractDwrModule;
import org.directwebremoting.guice.ParamName;
import org.mvel.optimizers.OptimizerFactory;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class ExampleModule extends AbstractDwrModule implements WarpModule, StartupListener {

    public void configure(Warp warp) {
        //install me as a module (convenience)
        warp.install(this);
        OptimizerFactory.setDefaultOptimizer("reflective");

        //install me as the startup listener (another hacky convenience -- note that some methods in this class will be called prior to injection!)
        //this is not a good example to follow in practice =). Ideally you would create your own startup listener impl(s)
        warp.addStartupListener(ExampleModule.class);

        warp.headers()
                .filter(DummyHtmlElementFilter.class)
                .filter(Key.get(DummyHtmlElementFilter.class));
    }

    @Singleton
    private static class DummyHtmlElementFilter implements HtmlElementFilter {
        public HtmlElement filter(HtmlElement element) {
            return element;
        }
    }


    protected void configure() {
        //bind custom impls here as you would in guice or dwr-guice
        //....

        bindRemoted(AjaxDemo.class).to(AjaxDemo.class);

        bindParameter(ParamName.DEBUG).to(true);

//        bindDwrScopes();
    }


    public void onStartup() {
        System.out.println("started up baby!");
    }
}
