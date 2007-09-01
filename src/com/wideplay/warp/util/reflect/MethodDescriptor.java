package com.wideplay.warp.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * On: 3/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class MethodDescriptor {
    private final Class<? extends Annotation> annotationClass;
    private final Annotation annotation;
    private final Method method;

    MethodDescriptor(Class<? extends Annotation> annotationClass, Annotation annotation, Method method) {
        this.annotationClass = annotationClass;
        this.annotation = annotation;
        this.method = method;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
}
