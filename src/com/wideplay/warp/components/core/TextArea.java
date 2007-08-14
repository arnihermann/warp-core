package com.wideplay.warp.components.core;

import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.util.beans.BeanUtils;
import com.google.inject.Injector;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 13, 2007
 * Time: 7:43:00 PM
 *
 * A component not unlike TextField, but that outputs a textarea instead. It doesnt do any
 * fancy rich text editing, so you can still use javascript rich text (of your choice) decorators
 * on top of this component.
 *
 */
public class TextArea implements Renderable, AttributesInjectable {
    private Map<String, Object> attribs;
    private String bind;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        Object[] attributes = (Object[]) attribs.get(RawText.WARP_RAW_TEXT_PROP_ATTRS);
        Object[] attrs = new Object[attributes.length + 2];

        attrs[0] = "name";
        attrs[1] = bind;

        System.arraycopy(attributes, 0, attrs, 2, attributes.length);

        writer.elementWithAttrs("textarea", attrs);
        writer.writeRaw(BeanUtils.getFromPropertyExpression(bind, page).toString());
        writer.end("textarea");
    }


    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }
}
