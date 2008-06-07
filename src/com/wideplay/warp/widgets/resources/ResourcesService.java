package com.wideplay.warp.widgets.resources;

import com.google.inject.ImplementedBy;
import com.wideplay.warp.widgets.Respond;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@ImplementedBy(ClasspathResourcesService.class)
public interface ResourcesService {
    void add(Class<?> clazz, Export export);

    Respond serve(String uri);
}
