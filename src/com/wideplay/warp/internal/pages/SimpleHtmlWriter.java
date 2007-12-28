package com.wideplay.warp.internal.pages;

import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.components.core.RawText;
import com.wideplay.warp.module.componentry.PropertyDescriptor;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
* User: dhanji
* Date: Dec 28, 2007
* Time: 2:31:22 PM
*
* @author Dhanji R. Prasanna (dhanji gmail com)
*/
class SimpleHtmlWriter {
    private final StringBuilder writer = new StringBuilder();

    public String newId(Object object) {
        return String.format("%s_%s", object.getClass().getSimpleName(), object.hashCode());
    }

    public String makeIdFor(AttributesInjectable object) {
        @SuppressWarnings("unchecked")
        PropertyDescriptor propertyDescriptor = ((Map<String, PropertyDescriptor>) object.getAttributeNameValuePairs()
                .get(RawText.WARP_RAW_TEXT_ATTR_MAP))
                .get("id");

        if (null == propertyDescriptor)
            return newId(object);

        return propertyDescriptor.getValue();
    }

    //convenience varargs method
    public void element(String name, Object...nameValuePairs) {
        elementWithAttrs(name, nameValuePairs);
    }

    //use this method to output as many sets of attribs as u need
    public void elementWithAttrs(String name, Object[] nameValuePairs) {
        writer.append('<');
        writer.append(name);
        attributes(nameValuePairs);
        writer.append('>');
    }


    public void writeRaw(String text) {
        writer.append(text);
    }

    private void attributes(Object[] attributeIterator) {

        for (int i = 0; i < attributeIterator.length; i += 2) {
            writer.append(' ');
            writer.append(attributeIterator[i]);
            writer.append("=\"");
            writer.append(attributeIterator[i + 1]);
            writer.append("\" ");
        }
    }

    public void end(String name) {

        writer.append("</");
        writer.append(name);
        writer.append('>');
    }

    public void selfClosedElement(String name, Object... nameValuePairs) {
        selfClosedElementWithAttrs(name, nameValuePairs);
    }

    public void selfClosedElementWithAttrs(String name, Object[] nameValuePairs) {
        writer.append('<');
        writer.append(name);
        attributes(nameValuePairs);
        writer.append("/>");
    }

    protected StringBuilder getWriter() {
        return writer;
    }

}
