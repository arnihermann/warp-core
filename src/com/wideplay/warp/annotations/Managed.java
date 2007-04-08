package com.wideplay.warp.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * This annotation is used on Page object fields that you want Warp to manage.
 * 
 * Typically this means keeping a property around at session scope. The scope can
 * be overridden for particular injection targets in your guice module by using:
 *
 * bind().to().in(scope)
 *
 * Setting autoCreate=true will tell Warp to attempt to inject an instance into the field
 * if it contains null at the beginning of the request. 
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Managed {
    boolean autoCreate() default true;
}
