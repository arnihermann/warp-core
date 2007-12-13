package com.wideplay.warp.rendering;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.Context;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.components.core.RawText;
import com.wideplay.warp.module.componentry.PropertyDescriptor;
import com.wideplay.warp.rendering.templating.HtmlElementFilter;
import com.wideplay.warp.rendering.templating.HtmlElement;
import com.wideplay.warp.rendering.templating.Headers;
import com.wideplay.warp.rendering.UnfilteredHtmlElement;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 27/08/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public abstract class AbstractHtmlWriter implements HtmlWriter {
    private final StringBuilder writer = new StringBuilder();

    @Inject @Context
    private HttpServletRequest request;

    private boolean header = false;

    @Inject @Headers
    private HtmlElementFilter elementFilterChain;

    public String newId(Object object) {
        return String.format("%s_%s", object.getClass().getSimpleName(), object.hashCode());
    }

    public String makeIdFor(AttributesInjectable object) {
        PropertyDescriptor propertyDescriptor = ((Map<String, PropertyDescriptor>) object.getAttributeNameValuePairs().get(RawText.WARP_RAW_TEXT_ATTR_MAP)).get("id");

        if (null == propertyDescriptor)
            return newId(object);

        return propertyDescriptor.getValue();
    }


    //TODO violates: Liskov Substitution Principle *bad*
    public void registerInputBinding(String name) {
    }

    //convenience varargs method
    public void element(String name, Object...nameValuePairs) {
        elementWithAttrs(name, nameValuePairs);
    }

    //use this method to output as many sets of attribs as u need
    public void elementWithAttrs(String name, Object[] nameValuePairs) {
        HtmlElement element = new UnfilteredHtmlElement(name, nameValuePairs);

        if ("head".equals(name))
            header = true;
        else if (header)
            element = filterHeaderElement(element);

        //hide element?
        if (null == element)
            return;

        writer.append('<');
        writer.append(element.getName());
        attributes(element.getAttributes());
        writer.append('>');
    }

    private HtmlElement filterHeaderElement(HtmlElement element) {
        //filter all header elements before writing them
        return elementFilterChain.filter(element);
    }

    public void writeRaw(String text) {
        writer.append(text);
    }

    private void attributes(HtmlElement.AttributeIterator attributeIterator) {

        while(attributeIterator.hasNext()) {
            writer.append(' ');
            writer.append(attributeIterator.next());
            writer.append("=\"");
            writer.append(attributeIterator.getValue());
            writer.append("\" ");
        }
    }

    public void end(String name) {
        //done writing header?
        if ("head".equals(name))
            header = false;

        writer.append("</");
        writer.append(name);
        writer.append('>');
    }

    public void selfClosedElement(String name, Object... nameValuePairs) {
        selfClosedElementWithAttrs(name, nameValuePairs);
    }

    public void selfClosedElementWithAttrs(String name, Object[] nameValuePairs) {
        HtmlElement element = new UnfilteredHtmlElement(name, nameValuePairs);

        if (header)
            element = filterHeaderElement(element);

        //hide element?
        if (null == element)
            return;

        writer.append('<');
        writer.append(element.getName());
        attributes(element.getAttributes());
        writer.append("/>");
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected StringBuilder getWriter() {
        return writer;
    }
}
