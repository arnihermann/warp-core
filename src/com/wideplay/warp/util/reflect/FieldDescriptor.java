package com.wideplay.warp.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * On: 19/02/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class FieldDescriptor {
    private final Class<? extends Annotation> annotationClass;
    private final Annotation annotation;
    private final Field field;
    private final Class<?> fieldType;
    private final Annotation[] otherAnnotationsOnField;


    FieldDescriptor(Class<? extends Annotation> annotationClass, Annotation annotation, Field field) {
        this.annotationClass = annotationClass;
        this.annotation = annotation;
        this.field = field;

        //discover other annotations and store the complement
        Set<Annotation> others = new HashSet<Annotation>();
        for (Annotation a : field.getAnnotations())
            if (!annotation.equals(a))
                others.add(a);

        otherAnnotationsOnField = others.toArray(new Annotation[others.size()]);

        //discover field type
        fieldType = field.getType();
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }


    public Annotation getAnnotation() {
        return annotation;
    }

    public Field getField() {
        return field;
    }

    public Annotation[] getOtherAnnotationsOnField() {
        return otherAnnotationsOnField;
    }


    public Class<?> getFieldType() {
        return fieldType;
    }
}
