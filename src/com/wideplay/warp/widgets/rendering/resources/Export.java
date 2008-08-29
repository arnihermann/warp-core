package com.wideplay.warp.widgets.rendering.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Mark classes with this to configure loading static resource from that class's neighborhood (i.e. using
 * {@code Class.getResourceAsStream}). Example:
 * </p>
 * 
 * <pre>
 * @Export(at="/my.js", "my.js")
 * public class MyWebPage { .. }
 * </pre>
 *
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Export {
    String at();
    String resource();
}
