package com.wideplay.warp.internal.pages;

import com.google.inject.BindingAnnotation;
import com.google.inject.Key;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 22/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class GuiceAnnotationsDiscovery {

    
    //scans a guice @Inject ctor for params and stores their binding annotations into the guice key list
    static List<Key<?>> discoverParameterAnnotations(Class<?>[] args, List<Key<?>> argKeys, Constructor injectable) {
        //account for the stupid case where theres an @Inject on a nullary ctor *rolls eyes*
        if (args.length > 0) {
            argKeys = new ArrayList<Key<?>>(args.length);
            Annotation[][] argsAnnotations = injectable.getParameterAnnotations();

            for (int i = 0; i < args.length; i++) {
                //does this param have annotations?
                if (argsAnnotations[i].length == 0) {
                    //no, add just the type and goto the next arg
                    argKeys.add(Key.get(args[i]));
                    continue;
                }

                //yes it does have, so scan for a @BindingAnnotation
                for (Annotation paramAnnotation : argsAnnotations[i]) {
                    if (paramAnnotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                        Key.get(args[i], paramAnnotation.annotationType());
                }
            }
        }

        return argKeys;
    }
}
