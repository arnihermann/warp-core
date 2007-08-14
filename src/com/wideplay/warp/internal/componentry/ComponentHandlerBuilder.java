package com.wideplay.warp.internal.componentry;

import com.wideplay.warp.components.core.RawText;
import com.wideplay.warp.components.core.Viewport;
import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.module.WarpConfigurationException;
import com.wideplay.warp.module.componentry.ComponentClassReflection;
import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.util.TextTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * Builds ComponentHandlers from a root Dom4j document.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class ComponentHandlerBuilder {
    private final ComponentRegistry registry;
    private static final String WARP_PREFIX = "w";  //TODO discover this from document namespace (or just test the namespace directly)

    private final Log log = LogFactory.getLog(ComponentHandlerBuilder.class);

    public ComponentHandlerBuilder(ComponentRegistry registry) {
        this.registry = registry;
    }

    //builds a component handler tree from an xhtml document
    public ComponentHandler build(Document document) {
        Element rootNode = document.getRootElement();

        //validate that this is an html template
        if (null == rootNode || !"html".equalsIgnoreCase(rootNode.getName()))
            throw new WarpConfigurationException("Warp can only handle html templates--no html root node was found!");

        //frame is generally built as the top level node
        return buildComponentHandler(rootNode);
    }




    //builds a component handler for any node having w: prefix, failing which it is treated as a text node
    private ComponentHandler buildComponentHandler(Node node) {
        boolean isRawText = false;

        //lookup the component name (we only worry about components marked with warp attribs)
        String componentName = null;

        //only element nodes (i.e. tags) can be warp-components
        if (Node.ELEMENT_NODE == node.getNodeType())
            componentName = node.valueOf("@w:component");


        if (log.isTraceEnabled())
            log.trace(String.format("Discovered node %s of type: %s", componentName, node));

        //we treat text and cdata nodes as RawText type
        if (Node.TEXT_NODE == node.getNodeType() || Node.CDATA_SECTION_NODE == node.getNodeType() || Node.COMMENT_NODE == node.getNodeType()

                //there was no w:component attribute so we treat this ELEMENT as a raw text component
                || (Node.ELEMENT_NODE == node.getNodeType() &&
                (null == componentName || "".equals(componentName.trim())) )   ) {

            log.debug("Text component discovered, building as RawText Component...");
            componentName = ComponentRegistry.TEXT_COMPONENT_NAME;
            isRawText = true;

        } else if (Node.ELEMENT_NODE != node.getNodeType() &&
                (null == componentName || "".equals(componentName.trim())) ) //not an element, text or cdata node, we dont care about it
            return null;


        //build a handler for either a Renderable or TemplateStyle component
        if (registry.isRenderableStyle(componentName))
            return buildRenderableComponentHandler(componentName, node, isRawText);
        else
            return buildTemplateStyleComponentHandler(componentName, node);
    }



    
    @SuppressWarnings("unchecked")
    private ComponentHandler buildTemplateStyleComponentHandler(String componentName, Node node) {
        //template-style components do not have children, so just build and return a handler
        Class<?> clazz = registry.getTemplateStyleComponent(componentName);

        //lookup viewport component
        ComponentClassReflection reflection = registry.getRenderableComponent(ComponentRegistry.VIEWPORT_COMPONENT_NAME);

        //read any warp-specific attributes and store them
        Map<String, PropertyDescriptor> propertyValueExpressions = buildPropertyValues(node, false);

        //read any arbitrary attributes
        Map<String, Object> arbitraryAttributes = buildCustomAttributes((Element) node);

        //configure viewport to bind on our component using its classname
        propertyValueExpressions.put(Viewport.EMBED_CLASS_PROPERTY, new PropertyDescriptor(Viewport.EMBED_CLASS_PROPERTY, clazz.getName(), false));

        return new ComponentHandlerImpl(reflection, propertyValueExpressions, Collections.EMPTY_LIST, arbitraryAttributes);
    }





    private ComponentHandlerImpl buildRenderableComponentHandler(String componentName, Node node, boolean rawText) {
        //lookup registered component (builds a reflection if needed)
        ComponentClassReflection reflection = registry.getRenderableComponent(componentName);

        //read warp-specific attributes and store them
        Map<String, PropertyDescriptor> propertyValueExpressions = buildPropertyValues(node, rawText);

        //read text & arbitrary attributes if necessary and store them
        Map<String, Object> arbitraryAttributes = null;
        if (reflection.isAttributesInjectable())
            arbitraryAttributes = buildArbitraryAttributes(node);

        //chain to children
        List<ComponentHandler> nestedComponentHandlers = buildChildNodes(node);

        //return when done
        return new ComponentHandlerImpl(reflection, propertyValueExpressions, nestedComponentHandlers, arbitraryAttributes);
    }

    private List<ComponentHandler> buildChildNodes(Node node) {
        //build nested components that are embedded below this ELEMENT recursively
        List<ComponentHandler> nestedComponentHandlers = new LinkedList<ComponentHandler>();
        if (Node.ELEMENT_NODE == node.getNodeType()) {
            Element element = (Element)node;

            if (log.isTraceEnabled())
                log.trace(String.format("Processing child nodes for <%s>", element.getName()));

            //iterate child nodes and recursively build their handlers
            for (Object object : element.content()) {
                Node child = (Node) object;

                //may build either as renderable or template-style
                ComponentHandler childHandler = buildComponentHandler(child);
                if (null != childHandler)
                    nestedComponentHandlers.add(childHandler);
            }
        }
        return nestedComponentHandlers;
    }


    //builds arbitrary custom attributes that are meant to be injected into user-defined non-Renderable @Component objects
    private Map<String, Object> buildCustomAttributes(Element element) {
        Map<String, Object> attribs = new LinkedHashMap<String, Object>();

        //walk attributes and stash them (so long as they're not warp components)
        for (Object object : element.attributes()) {
            Attribute attribute = (Attribute)object;

            //store only non-w: attribs
            if (!isWarpAttribute(attribute) && !attribute.getName().equals("component")) {
                PropertyDescriptor descriptor = buildPropertyDescriptor(attribute);

                //store the descriptor by property name
                attribs.put(descriptor.getName(), descriptor);
            }
        }

        return attribs;
    }


    //builds arbitrary attributes (random props that are stuck on to a component--really only for RawText components)
    private Map<String, Object> buildArbitraryAttributes(Node node) {
        Map<String, Object> attribs = new LinkedHashMap<String, Object>();

         //text components have a special property we assign called warpRawText
        attribs.put(RawText.WARP_RAW_TEXT_PROP_TOKENS, TextTools.tokenize(buildRawText(node)));

        //text components that are elements have a special property for <start> and <end> tags
        if (Node.ELEMENT_NODE == node.getNodeType()) {
            attribs.put(RawText.WARP_RAW_TEXT_PROP_TAG, node.getName());

            //text components that are elements have a special property that represents their dom attributes
            Element element = (Element)node;
            List<String> elementAttributes = new ArrayList<String>();
            for (Object object : element.attributes()) {
                Attribute attribute = (Attribute)object;

                //store attributes in a flat list
                elementAttributes.add(attribute.getName());
                elementAttributes.add(attribute.getValue());
            }

            attribs.put(RawText.WARP_RAW_TEXT_PROP_ATTRS, elementAttributes.toArray());
        }

        return attribs;
    }


    //builds w: prefixed attributes into a property-injection map
    private Map<String, PropertyDescriptor> buildPropertyValues(Node node, boolean isRawText) {
        Map<String, PropertyDescriptor> propertyValueExpressions = new LinkedHashMap<String, PropertyDescriptor>();

        if (!isRawText) {
            Element element = (Element)node;
            for (Object object : element.attributes()) {
                Attribute attribute = (Attribute) object;

                //store only w: attribs that are NOT component ids
                if (isWarpAttribute(attribute)) {
                    PropertyDescriptor descriptor = buildPropertyDescriptor(attribute);

                    //store the descriptor by property name
                    propertyValueExpressions.put(descriptor.getName(), descriptor);
                }
            }
        }

        return propertyValueExpressions;
    }

    private PropertyDescriptor buildPropertyDescriptor(Attribute attribute) {
        boolean isExpression = TextTools.isExpression(attribute.getValue());

        return new PropertyDescriptor(attribute.getName(),
                isExpression ? TextTools.stripExpression(attribute.getValue()) : attribute.getValue(),
                isExpression);
    }

    private boolean isWarpAttribute(Attribute attribute) {
        return attribute.getNamespacePrefix().equals(WARP_PREFIX) && !attribute.getName().endsWith("component");
    }

    //builds text that can be output from a RawText component specific to the given node
    private String buildRawText(Node node) {
        if (Node.COMMENT_NODE == node.getNodeType()
            || Node.CDATA_SECTION_NODE == node.getNodeType())
            return node.asXML();    //comments and cdata nodes cant have children

        //elements have their own child text nodes, so return nothing as their body
        if (Node.ELEMENT_NODE == node.getNodeType())
            return "";

        return node.getText();
    }

}
