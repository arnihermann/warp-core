package com.wideplay.warp.internal;

import com.google.inject.*;
import com.wideplay.warp.WarpModule;
import org.testng.annotations.Test;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class GuiceTest {

    public static class MyClass {
        @Inject List<String> strings;
        @Inject List<Integer> integers;
    }

    @Singleton
    public static class MyWiderScopeClass {
        @Inject MyNoScopeClass myClass;
    }

    public static class MyNoScopeClass {
        String name;
    }

    @Test
    public final void testTypeLiterals() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bindObject(String.class);
                bindObject(Integer.class);
            }

            private <T> void bindObject(Class<T> clazz) {
                bind(new TypeLiteral<List<T>>() { }).toInstance(new LinkedList<T>());
            }
        });

        MyClass my = injector.getInstance(MyClass.class);

        my.strings.add("hello");

        assert my.integers.isEmpty();
    }

    //just some tests to see how guice behaves
    @Test
    public final void testInjections() {
        Injector injector = Guice.createInjector();

        //on a java.lang
        Object o = injector.getInstance(String.class);

        assert o instanceof String;
        System.out.println(MessageFormat.format("[{0}]", o));
    }


    //just some tests to see how guice behaves
    @Test
    public final void testInjectionsWithModule() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                
            }
        });
        assert null == injector.getBinding(Key.get(WarpModule.class));
    }

    //just some tests to see how guice behaves
    @Test
    public final void testWideningScopeInjection() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {

            }
        });

        MyWiderScopeClass myWiderScopeClass = injector.getInstance(MyWiderScopeClass.class);

        assert myWiderScopeClass == injector.getInstance(MyWiderScopeClass.class) : "singleton violated";

        assert myWiderScopeClass.myClass == injector.getInstance(MyWiderScopeClass.class).myClass :
               "singleton's down-scoped instance was different";
    }


    //just some tests to see how guice behaves
    @Test
    public final void testTypelessProvider() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @SuppressWarnings("unchecked")
            protected void configure() {
                bind(GuiceTest.class).toProvider(new Provider() {
                    public Object get() {
                        return "hi";
                    }
                });
            }
        });

        injector.getInstance(GuiceTest.class);
    }

    public static <T> T get() {
        return null;
    }

}
