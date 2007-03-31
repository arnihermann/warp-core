package com.wideplay.warp.internal.pages;

import com.google.inject.*;
import com.google.inject.cglib.proxy.Enhancer;
import com.google.inject.cglib.proxy.Factory;
import com.google.inject.cglib.proxy.MethodInterceptor;
import com.google.inject.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class PageClassReflectionBuilderTest {
    private static final String SOMETHING = "something";
    private static final String SOMETHING_ELSE = "somethingelse";
    private static final String ANOTHER_THING_ENTIRELY = "another thing entirely";
    private static final String A_PROXY_SOMETHING = "aproxy thing entirely";

    private final Log log = LogFactory.getLog(getClass());

    @Test
    public void testBuildPageClassReflection() {
        PageClassReflectionImpl reflection = new PageClassReflectionBuilder(PCRBTestPage.class).build();

        Map<String, Method> getters = reflection.getGetters();
        Map<String, Method> setters = reflection.getSetters();

        for (String property : getters.keySet()) {
            log.info("Property: " + property + " has getter: " + getters.get(property));
        }

        assert getters.containsKey("hello") : "Did not contain getter for expected property hello";
        assert getters.containsKey("title") : "Did not contain getter for expected property title";

        assert "getHello".equals(getters.get("hello").getName()) : "Getter for expected property hello was not valid";
        assert "getTitle".equals(getters.get("title").getName()) : "Getter for expected property title was not valid";


        for (String property : setters.keySet()) {
            log.info("Property: " + property + " has setter: " + setters.get(property));
        }

        assert !setters.containsKey("hello") : "Contain setter for readonly property hello";
        assert setters.containsKey("title") : "Did not contain setter for expected property title";

        assert "setTitle".equals(setters.get("title").getName()) : "Setter for expected property title was not valid";

    }

    @Test
    public void testGuiceClassToSubClassBinding() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bind(MySuper.class).to(MySub.class);
            }
        });

        String something = injector.getInstance(MySuper.class).getSomething();
        assertEquals(SOMETHING_ELSE, something);
        System.out.println(something);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGuiceClassToProxyProviderBinding() {
        //create proxy for MySuper
        final Object proxy = Enhancer.create(MySuper.class, new Class[] { PageIdentity.class },
            new MethodInterceptor() {

            public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if (PageIdentity.class.getDeclaredMethod(PageIdentity.WARP_PAGE_ID_GETTER).equals(method))
                    return A_PROXY_SOMETHING;

                //otherwise proceed
                return methodProxy.invokeSuper(object, args);
            }
        });

        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bind(MySuper.class).toProvider(new Provider<MySuper>() {
                    @Inject Injector injector;
                    public MySuper get() {
                        MySuper my = (MySuper) proxy;
                        injector.injectMembers(my);

                        return my;
                    }
                });
            }
        });

        //test that it returns the normal thing for unintercepted methods
        String something = injector.getInstance(MySuper.class).getSomething();
        assertEquals(something, SOMETHING);
        System.out.println(something);

        //make sure getBuffer and other stuff is not overridden
        assert !ANOTHER_THING_ENTIRELY.equals(injector.getInstance(MySuper.class).toString()) : "proxy was not supposed to intercept all methods but it did =(";
        Object my = injector.getInstance(MySuper.class);

        //assert the right interface is added to proxy
        assert my instanceof PageIdentity : "object was not proxied with the expected interface PageIdentity";
        PageIdentity myPageIdentity = (PageIdentity)my;

        //assert that the interface method is intercepted properly
        assert A_PROXY_SOMETHING.equals(myPageIdentity.__warpPageIdentity()) : "proxy did not return the expected value for the intercepted method";

        assert null != ((MySuper)my).arbitrary : "proxy was not injected as expected by guice";
    }


    @Deprecated //(doesnt work and not really relevant anymore)
    @SuppressWarnings("unchecked")
    public void testGuiceClassToProxyClassBinding() {
        //create proxy for MySuper
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MySuper.class);
        enhancer.setInterfaces(new Class[] { PageIdentity.class });
        enhancer.setCallbackType(MethodInterceptor.class);

        final Class<?> proxyClass = enhancer.createClass();

        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bind(MySuper.class).to((Class<? extends MySuper>) proxyClass);
            }
        });

        //test that it returns the normal thing for unintercepted methods
        MySuper mySuper = injector.getInstance(MySuper.class);
        String something = mySuper.getSomething();

        //modify something:
        Factory factory = (Factory)mySuper;
        factory.setCallback(1, new MethodInterceptor() {

            public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if (PageIdentity.class.getDeclaredMethod(PageIdentity.WARP_PAGE_ID_GETTER).equals(method))
                    return A_PROXY_SOMETHING;

                //otherwise proceed
                return methodProxy.invokeSuper(object, args);
            }
        });

        assertEquals(something, SOMETHING);
        System.out.println(something);

        //make sure getBuffer and other stuff is not overridden
        assert !ANOTHER_THING_ENTIRELY.equals(injector.getInstance(MySuper.class).toString()) : "proxy was not supposed to intercept all methods but it did =(";
        Object my = injector.getInstance(MySuper.class);

        //assert the right interface is added to proxy
        assert my instanceof PageIdentity : "object was not proxied with the expected interface PageIdentity";
        PageIdentity myPageIdentity = (PageIdentity)my;

        //assert that the interface method is intercepted properly
        assert A_PROXY_SOMETHING.equals(myPageIdentity.__warpPageIdentity()) : "proxy did not return the expected value for the intercepted method";

        assert null != ((MySuper)my).arbitrary : "proxy was not injected as expected by guice";
    }








    
    public static class MySuper {
        @Inject Arbitrary arbitrary;
        
        public String getSomething() {
            return SOMETHING;
        }
    }

    public static class MySub extends MySuper {

        public String getSomething() {
            return SOMETHING_ELSE;
        }
    }

    public static class Arbitrary {

    }
}
