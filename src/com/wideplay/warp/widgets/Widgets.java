package com.wideplay.warp.widgets;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.wideplay.warp.servlet.Servlets;
import com.wideplay.warp.widgets.basic.CaseWidget;

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

            public PackageAddingBuilder with(Package pack) {
                packages.add(pack);

                return this;
            }

            public Module buildModule() {
                //noinspection InnerClassTooDeeplyNested
                return new AbstractModule() {

                    @Override
                    protected void configure() {
                        //insert basic widgets set
                        packages.add(0, CaseWidget.class.getPackage());

                        //noinspection InnerClassTooDeeplyNested
                        bind(new TypeLiteral<Set<Package>>() {})
                                .annotatedWith(Packages.class)
                                .toInstance(Collections.unmodifiableSet(new LinkedHashSet<Package>(packages)));

                        bind(ContextInitializer.class);

                        Servlets.bindScopes(binder());
                    }
                };
            }
        };
    }

    public static interface PackageAddingBuilder {

        PackageAddingBuilder with(Package pack);

        Module buildModule();
    }
}
