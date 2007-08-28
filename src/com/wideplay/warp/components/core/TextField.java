package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class TextField implements Renderable, AttributesInjectable {
    private String bind;
    private Map<String, Object> injectableAttributes;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        //read the bound value
        Object text = BeanUtils.getFromPropertyExpression(bind, page);

        String id = writer.makeIdFor(this);
        writer.registerInputBinding(id);

        //find css class from injectables
        String cssClass;
        Object[] attrs = (Object[]) injectableAttributes.get(RawText.WARP_RAW_TEXT_PROP_ATTRS);
        cssClass = ComponentSupport.discoverCssAttribute(attrs);

        if (null == cssClass)
            cssClass = "wText";

        writer.element("input",
                "id", id,
                "type", "text",
                "name", bind,
                "value", text,
                "class", cssClass);
        writer.end("input");
    }


    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }


    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.injectableAttributes = attribs;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return injectableAttributes;
    }
}
