package com.wideplay.warp.widgets;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Objects;
import com.wideplay.warp.servlet.Servlets;
import com.wideplay.warp.widgets.core.CaseWidget;
import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.widgets.routing.RoutingDispatcher;
import org.mvel.MVEL;

import java.util.*;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public final class Widgets {
    private Widgets() {
    }

    public static PackageAddingBuilder configure() {

        return new PackageAddingBuilder() {
            private final List<Package> packages = new ArrayList<Package>();
            private Options options;

            public PackageAddingBuilder with(Package pack) {
                packages.add(pack);

                return this;
            }

            public PackageAddingBuilder set(Options options) {
                Objects.nonNull(options, "set() called with null. You must use the Widgets.options() to configure " +
                        "warp-widgets options.");

                return this;
            }

            public Module buildModule() {
                //set default options?
                if (null == options)
                    this.options = options().build();


                //noinspection InnerClassTooDeeplyNested
                return new AbstractModule() {

                    @Override
                    protected void configure() {

                        //set up MVEL namespace (when jarjar-ed, it will use the repackaged namespace)
                        System.setProperty("mvel.namespace",
                                MVEL.class.getPackage().getName().replace('.', '/') + "/");

                        //configuration options
                        bind(Options.class).toInstance(options);

                        //insert core widgets set
                        packages.add(0, CaseWidget.class.getPackage());

                        //noinspection InnerClassTooDeeplyNested
                        bind(new TypeLiteral<Set<Package>>() {})
                                .annotatedWith(Packages.class)
                                .toInstance(Collections.unmodifiableSet(new LinkedHashSet<Package>(packages)));

                        //initialize startup services and routing modules
                        bind(ContextInitializer.class).asEagerSingleton();
                        install(PageBook.Routing.module());


                        //development mode services
                        if (Stage.DEVELOPMENT.equals(binder().currentStage())) {
                            bind(PageBook.class).to(DebugModePageBook.class);
                            bind(RoutingDispatcher.class).to(DebugModeRoutingDispatcher.class);
                        }

                        Servlets.bindScopes(binder());

                    }
                };
            }
        };
    }


    /**
     *
     * @return Returns an options builder to set options with
     */
    public static OptionsBuilder options() {
        return new Options();
    }


    /**
     * Part of the EDSL for configuring warp-widgets.
     */
    public static interface PackageAddingBuilder {

        PackageAddingBuilder with(Package pack);

        PackageAddingBuilder set(Options options);

        Module buildModule();
    }


    /**
     * Part of the EDSL for configuring warp-widgets optional settings.
     */
    public static interface OptionsBuilder {
        OptionsBuilder contextualizeUris();

        OptionsBuilder trimTemplateText();

        OptionsBuilder elevateWarnings();

        OptionsBuilder ignoreComments();

        OptionsBuilder noDebugPage();

        Options build();
    }
}
