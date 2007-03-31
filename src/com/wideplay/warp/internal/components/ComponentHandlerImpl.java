package com.wideplay.warp.internal.components;

import com.google.inject.Injector;
import com.wideplay.warp.core.AttributesInjectable;
import com.wideplay.warp.module.components.ComponentClassReflection;
import com.wideplay.warp.module.components.PropertyDescriptor;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.PageRenderException;
import com.wideplay.warp.rendering.HtmlWriter;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class ComponentHandlerImpl implements ComponentHandler {
    private final ComponentClassReflection reflection;
    private final Map<String, PropertyDescriptor> propertyValueExpressions;
    private final List<? extends ComponentHandler> nestedComponents;
    private final Map<String, Object> arbitraryAttributes;

    public ComponentHandlerImpl(ComponentClassReflection reflection, Map<String, PropertyDescriptor> propertyValues,
                            List<ComponentHandlerImpl> nestedComponents, Map<String, Object> arbitraryAttributes) {
        
        this.reflection = reflection;
        this.propertyValueExpressions = Collections.unmodifiableMap(propertyValues);
        this.nestedComponents = Collections.unmodifiableList(nestedComponents);
        this.arbitraryAttributes = arbitraryAttributes;
    }


    public void handleRender(HtmlWriter writer, Injector injector, PageClassReflection pageReflection, Object page) {
        //obtain the renderable object from guice (makes sure it is properly injected)
        Renderable renderable = injector.getInstance(reflection.getComponentClass());

        //fire lifecycle method pre-render?
        //...

        //bind the page model (ognl expressions) to the components attributes
        for (String property : propertyValueExpressions.keySet()) {
            try {
                //rip out value from page object and set it to component's attribute (where the ognl expression came from)
                PropertyDescriptor propertyDescriptor = propertyValueExpressions.get(property);
                Object value;
                if (propertyDescriptor.isExpression())
                    value =  Ognl.getValue(propertyDescriptor.getValue(), page);
                else
                    value = propertyDescriptor.getValue();
                
                //set the property on the component object
                reflection.setPropertyValue(renderable, property, value);
            } catch (OgnlException e) {
                throw new PageRenderException("Error while evaluating expression: \"" + propertyValueExpressions.get(property).getValue() + "\" for attribute " + property, e);
            }
        }

        if (renderable instanceof AttributesInjectable)
            ((AttributesInjectable)renderable).setAttributeNameValuePairs(arbitraryAttributes);

        //render renderable template with the renderable object as its model
        renderable.render(writer, nestedComponents, injector, pageReflection, page);
    }


    public List<? extends ComponentHandler> getNestedComponents() {
        return nestedComponents;
    }
}
