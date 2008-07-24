package com.wideplay.warp.widgets;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Utility class helps XmlTemplateCompiler work with the DOM.
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class Dom {
    static final String FORM_TAG = "form";
    public static final Pattern WIDGET_ANNOTATION_REGEX = Pattern.compile("(@\\w\\w*(\\([\\w,=\"'()?:><!\\[\\];{}. ]*\\))?[ \n\r\t]*)\\Z");
    static final String XMLNS_ATTRIB_REGEX = " xmlns=\"[a-zA-Z0-9_+%;#/\\-:\\.]*\"";

    private Dom() {
    }


    //is this a form node?
    static boolean isForm(Node node) {
        return FORM_TAG.equals(node.getName());
    }

    static String stripAnnotation(String text) {
        final Matcher matcher = WIDGET_ANNOTATION_REGEX
                .matcher(text);

        //strip off the ending bit (annotation)
        if (matcher.find())
            return text.substring(0, matcher.start());

        return text;
    }

    static String readAnnotation(Node node) {
        String annotation = null;

        //if this is a text node, then match for annotations
        if (isText(node)) {
            final Matcher matcher = WIDGET_ANNOTATION_REGEX
                    .matcher(node.asXML());

            if (matcher.find()) {
                annotation = matcher.group();
            }
        }
        return annotation;
    }

    static String asRawXml(Element element) {
        return element.asXML().replaceFirst(XMLNS_ATTRIB_REGEX, "");
    }

    static boolean skippable(Attribute type) {
        if (null == type)
            return false;

        final String kind = type.getValue();
        return ( "submit".equals(kind) || "button".equals(kind) || "reset".equals(kind) || "file".equals(kind) );
    }

    /**
     *
     * @param list A list of dom4j attribs
     * @return Returns a mutable map parsed out of the dom4j attribute list
     */
    static Map<String, String> parseAttribs(List list) {
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

        String content = annotation.substring(index + 1, annotation.lastIndexOf(')'));

        //normalize empty string to null
        if ("".equals(content))
            content = null;

        return new String[] { annotation.substring(1, index).toLowerCase(), content };
    }

    static boolean isTextCommentOrCdata(Node node) {
        final short nodeType = node.getNodeType();

        return isText(node) || Node.COMMENT_NODE == nodeType || Node.CDATA_SECTION_NODE == nodeType;
    }

    static boolean isText(Node preceeding) {
        return null != preceeding && Node.TEXT_NODE == preceeding.getNodeType();
    }

    static boolean isElement(Node node) {
        return Node.ELEMENT_NODE == node.getNodeType();
    }
}
