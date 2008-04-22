package com.wideplay.warp.widgets;

import org.dom4j.*;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class XmlTemplateParser {
    private final Evaluator evaluator;
    private final WidgetRegistry registry;
    public static final Pattern WIDGET_ANNOTATION_REGEX = Pattern.compile("(@\\w\\w*(\\([\\w,=\" ]*\\))?[ \n\r\t]*)$");
//    public static final Pattern WIDGET_ANNOTATION_REGEX = Pattern.compile("@\\w\\w*(\\([\\w,=\" ]*\\))?");

    public XmlTemplateParser(Evaluator evaluator, WidgetRegistry registry) {
        this.evaluator = evaluator;
        this.registry = registry;
    }

    public RenderableWidget parse(String template) {
        WidgetChain widgetChain;
        try {
            widgetChain = walk(DocumentHelper.parseText(template));
        } catch (DocumentException e) {
            throw new TemplateParseException(e);
        }

        return widgetChain;
    }

    private WidgetChain walk(Document document) {
        WidgetChain chain = new WidgetChain();
        final WidgetChain docChain = walk(document.getRootElement());

        chain.addWidget(widgetize(null, document.getRootElement(), docChain));

        return chain;
    }

    /**
     *  Walks the DOM recursively, and converts elements into corresponding warp-widgets
     */
    @SuppressWarnings({"JavaDoc"}) @NotNull
    private WidgetChain walk(Element element) {
        WidgetChain widgetChain = new WidgetChain();
        
        for (int i = 0, size = element.nodeCount(); i < size; i++) {
            Node node = element.node(i);

            if (isElement(node)) {

                final Element child = (Element) node;

                //continue recursing down
                WidgetChain childsChildren = walk(child);

                //process
                if (i > 0)
                    widgetChain.addWidget(widgetize(element.node(i - 1), child, childsChildren));
                else
                    widgetChain.addWidget(widgetize(null, child, childsChildren));


            } else if (isTextCommentOrCdata(node)) {
                //process as raw text widget
                widgetChain.addWidget(new TextWidget(stripAnnotation(node.asXML()), evaluator));
            }
        }

        //return computed chain, or a terminal
        return widgetChain;
    }

    private static String stripAnnotation(String text) {
        final Matcher matcher = WIDGET_ANNOTATION_REGEX
                .matcher(text);

        //strip off the ending bit (annotation)
        if (matcher.find())
            return text.substring(0, matcher.start());

        return text; 
    }


    /**
     * This method converts an xml element into a specific kind of warp-widget
     */
    @SuppressWarnings({"JavaDoc"}) @NotNull
    private RenderableWidget widgetize(Node preceeding, Element element, WidgetChain childsChildren) {
        String annotation = null;

        //read annotation if available
        if (isText(preceeding)) {
            final Matcher matcher = WIDGET_ANNOTATION_REGEX
                    .matcher(preceeding.getText());
            
            if (matcher.find())
                annotation = matcher.group();
            
        }

        //if there is no annotation, treat as a raw xml-widget (i.e. tag)
        if (null == annotation)
            return new XmlWidget(childsChildren, element.getName(), evaluator, parseAttribs(element.attributes()));

        //process as "normal" widget
        String[] extract = extractKeyAndContent(annotation);

        //normalize empty string to null
        if ("".equals(extract[1]))
            extract[1] = null;


        //if this is NOT a self-rendering widget, give it an xml child
        final String widgetName = extract[0];
        if (!registry.isSelfRendering(widgetName))
            childsChildren = new SingleWidgetChain(
                             new XmlWidget(childsChildren, element.getName(), evaluator,
                                    parseAttribs(element.attributes())));

        //key, expression, child widgets
        return registry.newWidget(widgetName, extract[1], childsChildren);
    }

    /**
     *
     * @param list A list of dom4j attribs
     * @return Returns a mutable map parsed out of the dom4j attribute list
     */
    private Map<String, String> parseAttribs(List list) {
        Map<String, String> attrs = new LinkedHashMap<String, String>(list.size() + 4);

        for (Object o : list) {
            Attribute attribute = (Attribute)o;

            attrs.put(attribute.getName(), attribute.getValue());
        }

        return attrs;
    }


    static String[] extractKeyAndContent(String annotation) {
        final int index = annotation.indexOf('(');

        //there's no content
        if (index < 0)
            return new String[] { annotation.substring(1).toLowerCase(), "" };

        return new String[] { annotation.substring(1, index).toLowerCase(),
                              annotation.substring(index + 1, annotation.lastIndexOf(')')) };
    }


    private static boolean isTextCommentOrCdata(Node node) {
        final short nodeType = node.getNodeType();
        
        return isText(node) || Node.COMMENT_NODE == nodeType || Node.CDATA_SECTION_NODE == nodeType;
    }

    private static boolean isText(Node preceeding) {
        return null != preceeding && Node.TEXT_NODE == preceeding.getNodeType();
    }

    private static boolean isElement(Node node) {
        return Node.ELEMENT_NODE == node.getNodeType();
    }
}
