package com.wideplay.warp.internal.componentry;

import com.google.inject.Injector;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.componentry.ComponentClassReflection;
import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.IocContextManager;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.RenderingContext;

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
                                List<ComponentHandler> nestedComponents, Map<String, Object> arbitraryAttributes) {
        
        this.reflection = reflection;
        this.propertyValueExpressions = Collections.unmodifiableMap(propertyValues);
        this.nestedComponents = Collections.unmodifiableList(nestedComponents);
        this.arbitraryAttributes = arbitraryAttributes;
    }


    public void handleRender(RenderingContext context) {
        //obtain the renderable object from guice (makes sure it is properly injected)
        Injector injector = context.getInjector();
        Renderable renderable = injector.getInstance(reflection.getComponentClass());

        //fire lifecycle method pre-render?
        //...

        //bind the page model (mvel expressions) to the component's attributes
        Object page = context.getPage();
        IocContextManager.injectProperties(propertyValueExpressions.values(), renderable, context.getContextVars());

        //pass through arbitrary non-warp attributes for the component to do with as it likes
        if (renderable instanceof AttributesInjectable)
            ((AttributesInjectable)renderable).setAttributeNameValuePairs(arbitraryAttributes);

        //render renderable template with the renderable object as its model
        renderable.render(context, nestedComponents);
    }


    public Map<String, PropertyDescriptor> getPropertyValueExpressions() {
        return propertyValueExpressions;
    }

    public List<? extends ComponentHandler> getNestedComponents() {
        return nestedComponents;
    }

    public ComponentClassReflection getComponentClassReflection() {
        return reflection;
    }
}
