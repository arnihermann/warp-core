package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.PageRenderException;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * Projects enclosed components across a collection of items (akin to map function in lisp).
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class Repeater implements Renderable {
    private Object items;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {

        //obtain the bound object
        Object itemsObject = items; //BeanUtils.getFromPropertyExpression(items, page);

        //see if it is an iterable
        if (itemsObject instanceof Iterable)
            for (Object item : (Iterable) itemsObject)
                ComponentSupport.renderMultiple(writer, nestedComponents, injector, null, item);

        else if (itemsObject instanceof Object[])
            //must be an array
            for (Object item : (Object[])itemsObject)
                ComponentSupport.renderMultiple(writer, nestedComponents, injector, null, item);
        else 
            throw new PageRenderException("Repeater can only repeat over instances of Iterables or arrays: " + items);

    }


    public void setItems(Object items) {
        this.items = items;
    }
}
