package com.wideplay.warp.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * On: 21/03/2007
 *
 * This annotation is used to signal to guice that the object being provided is a
 * warp page and should be provided by warp (without subsequent pages set).
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.PARAMETER } )
@BindingAnnotation
public @interface Page {
}
