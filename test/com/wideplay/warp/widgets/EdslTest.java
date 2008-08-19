package com.wideplay.warp.widgets;

import com.google.inject.*;
import static org.easymock.EasyMock.createNiceMock;

import javax.servlet.ServletContext;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class EdslTest {

//    @Test
    public final void edsl() {
        Package aPackage = com.wideplay.warp.widgets.example.WidgetStartup.class.getPackage();
        Package anotherPackage = com.wideplay.warp.widgets.example.WidgetStartup.class.getPackage();

        assert null != aPackage;
        assert null != anotherPackage;

        final Module module = Widgets.configure()
                .with(aPackage)
                .with(anotherPackage)

                .buildModule();

        assert null != module : "nothing built out of config";

        final Set<Package> packages = Guice.createInjector(module, new AbstractModule() {
            protected void configure() {
                bind(ServletContext.class).toInstance(createNiceMock(ServletContext.class));
            }
        })
                .getInstance(Key.get(new TypeLiteral<Set<Package>>() {}, Packages.class));

        assert null != packages;
        assert packages.contains(aPackage) : "package binding incorrect!";
        assert packages.contains(anotherPackage) : "package binding incorrect!";
    }
}
