package com.wideplay.warp.internal.componentry;

import com.wideplay.warp.components.core.RawText;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.componentry.ComponentClassReflection;
import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.util.TextTools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 7:50:01 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public class SimpleTextComponentHandlerBuilder {
    private final ComponentRegistry registry;

    public SimpleTextComponentHandlerBuilder(ComponentRegistry registry) {
        this.registry = registry;
    }


    public ComponentHandler build(String documentText) {
        //build this as a single, flat raw-text component
        final ComponentClassReflection reflection = registry.getRenderableComponent(ComponentRegistry.TEXT_COMPONENT_NAME);

        //no child components (flat template structure)
        final List<ComponentHandler> nestedComponentHandlers = Collections.emptyList();

        //this raw text component can't really have any properties either =(
        final Map<String, PropertyDescriptor> propertyValueExpressions = Collections.emptyMap();

         //text components have a special property we assign called warpRawText
        final Map<String, Object> injectableAttributes = tokenizeDocument(documentText);

        //wrap with a component handler and return when done
        return new ComponentHandlerImpl(reflection, propertyValueExpressions, nestedComponentHandlers, injectableAttributes);
    }

    private Map<String, Object> tokenizeDocument(String documentText) {
        final Map<String, Object> injectableAttributes = new HashMap<String, Object>();

        injectableAttributes.put(RawText.WARP_RAW_TEXT_PROP_TOKENS, TextTools.tokenize(documentText));

        return injectableAttributes;
    }
}
