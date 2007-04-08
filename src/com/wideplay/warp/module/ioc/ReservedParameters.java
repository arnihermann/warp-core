package com.wideplay.warp.module.ioc;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * On: 25/03/2007
 *
 * Annotation indicates request parameters that are reserved by warp (and should not be bound).
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.PARAMETER } )
@BindingAnnotation
public @interface ReservedParameters {
}
