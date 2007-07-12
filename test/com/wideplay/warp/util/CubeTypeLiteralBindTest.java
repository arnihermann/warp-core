package com.wideplay.warp.util;

import com.google.inject.*;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 13/06/2007
 * Time: 15:56:33
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
public class CubeTypeLiteralBindTest {
    private static final TypeLiteral<Cube<Class<?>, String, Object>> cubeLiteral = new TypeLiteral<Cube<Class<?>, String, Object>>() { };

    @Test
    public final void testTypeLiterals() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            protected void configure() {
                bind(new TypeLiteral<Cube<Class<?>, String, Object>>() { })
                .to(new TypeLiteral<HashCube<Class<?>, String, Object>>() { }).in(Singleton.class);
            }
        });

        Cube<Class<?>, String, Object> cube = injector.getInstance(Key.get(cubeLiteral));
        assert null != cube;
     }
}
