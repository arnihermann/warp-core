package com.wideplay.warp.components.core;

import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.el.Expressions;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;

import java.util.List;
import java.util.Map;

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

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        String id = writer.makeIdFor(this);

        writer.registerInputBinding(id);
        writer.elementWithAttrs("textarea",
                ComponentSupport.getTagAttributesExcept(new Object[] { "id", id, "name", bind }, attribs, "name"));
        writer.writeRaw(Expressions.evaluate(bind, context.getContextVars()).toString());
        writer.end("textarea");
    }

    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return attribs;
    }
}
