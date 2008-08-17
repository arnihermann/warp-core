package com.wideplay.warp.widgets.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.wideplay.warp.servlet.Servlets;
import com.wideplay.warp.servlet.WarpServletContextListener;
import com.wideplay.warp.widgets.WidgetFilter;
import com.wideplay.warp.widgets.Widgets;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class WidgetStartup extends WarpServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(Stage.DEVELOPMENT, Servlets.configure()
                .filters()
                    .filter("/*").through(WidgetFilter.class)
                .buildModule(),

                Widgets.configure()
                    .with(WidgetStartup.class.getPackage())
                        
                    .buildModule()
        );
    }
}
