package com.wideplay.warp.components.core;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.ClassReflectionCache;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Jul 14, 2007
 * Time: 5:38:31 PM
 *
 * This is a "Bean Editor" component. It reflects on and produces a set of input components to
 * edit a provided javabean. Its introspection mechanism is similar to the Table component.
 */
@Component
public class Edit implements Renderable {
    private String object;
    private int columns;

    private final ClassReflectionCache cache;

    @Inject
    public Edit(ClassReflectionCache cache) {
        this.cache = cache;
    }

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        //read bean (to edit) out of page
        Object theBean = BeanUtils.getFromPropertyExpression(object, context.getContextVars());

        //if the bean value is null, then do nothing
        if (null == theBean)
            return;

        writer.element("fieldset");

        Map<String, String> propertiesAndLabels = cache.getPropertyLabelMap(theBean);

        Map<String, Class<?>> propertiesAndTypes = cache.getPropertyTypeMap(theBean);

        for (Map.Entry<String, Class<?>> propertyType : propertiesAndTypes.entrySet()) {
            //TODO complete
        }

        writer.end("fieldset");
    }


    public void setObject(String object) {
        this.object = object;
    }

    public int getColumns() {
        return columns;
    }
}
