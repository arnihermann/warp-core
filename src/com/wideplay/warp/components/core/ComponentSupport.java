package com.wideplay.warp.components.core;

import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 8:53:18 PM
 *
 * A simple utility that is meant to factor out boilerplate for rendering child components.
 *
 * @author Dhanji R. Prasanna
 */
public class ComponentSupport {


    private ComponentSupport() {
    }

    /**
     * A utility method that helps dispatch rendering to a list of ComponentHandlers
     */
    public static void renderMultiple(RenderingContext context, List<? extends ComponentHandler> componentHandlers
    ) {
        for (ComponentHandler handler : componentHandlers)
            handler.handleRender(context);
    }

    static String discoverCssAttribute(Object[] attrs) {
        for (int i = 0; i < attrs.length; i++) {
            String attr = (String)attrs[i];

            if ("class".equals(attr)) {
                return (String) attrs[i + 1];
            }
        }

        return null;
    }

    //returns everything except the specified "excepts"
    static Object[] getTagAttributesExcept(Map<String, Object> attribs, String...excepts) {
        //iterative copy (improve?)
        Object[] attributes = (Object[]) attribs.get(RawText.WARP_RAW_TEXT_PROP_ATTRS);

        if (null == attributes)
            return BeanUtils.EMPTY_ARRAY;

        List<String> targetAttributes = new ArrayList<String>(attributes.length);

        for (int i = 0; i < attributes.length; i++) {
            String attribute = (String) attributes[i];

            if (isExcept(attribute, excepts)) {
                i += 2;
            } else {
                targetAttributes.add(attribute);
                targetAttributes.add((String) attributes[i + 1]);
                i++;
            }
        }

        return targetAttributes.toArray();
    }

    private static boolean isExcept(String attribute, String... excepts) {
        for (String except : excepts) {
            //skip this and the next
            if (except.equalsIgnoreCase(attribute)) {
                return true;
            }
        }

        return false;
    }

    public static List<? extends ComponentHandler> obtainFrameNestedContent(List<? extends ComponentHandler> nestedComponents) {
        for (ComponentHandler componentHandler : nestedComponents)
            if (Frame.class.isAssignableFrom(componentHandler.getComponentClassReflection().getComponentClass()))
                return componentHandler.getNestedComponents();

        return null;    //not found!
    }
}
