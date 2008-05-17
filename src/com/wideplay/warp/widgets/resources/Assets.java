package com.wideplay.warp.widgets.resources;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * <p>
 * Use to export multiple @Export resources.
 * </p>
 *
 * <pre>
 * @Assets({@Export(at="/my.js", "my.js"), ... })
 * public class MyWebPage { .. }
 * </pre>
 *
 *
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Assets {
    Export[] value();
}