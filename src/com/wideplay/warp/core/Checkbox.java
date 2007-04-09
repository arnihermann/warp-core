package com.wideplay.warp.core;

import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.beans.BeanUtils;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.annotations.Component;
import com.google.inject.Injector;

import java.util.List;

import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: josh
 * Date: Apr 6, 2007
 * Time: 12:23:33 PM
 *
 * Simple boolean (for now) checkbox
 *
 */
@Component
public class Checkbox implements Renderable {

    private String event;
    private String label = TextTools.EMPTY_STRING;
    private String bind;

    private Logger log = Logger.getLogger(Checkbox.class);

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {

        String encodedEvent = TextTools.EMPTY_STRING;

        if (null != event)
            encodedEvent = event;

        log.debug("binding to:" + bind);

        boolean booleanValue=false;

        if (null != bind) {
            Object objValue = BeanUtils.getFromPropertyExpression(bind, page);

            if(null != objValue) {
                log.debug("bind objValue instanceof " + objValue.getClass().getName() + " objValue=" + objValue);
                if (objValue instanceof Boolean)
                    booleanValue = (Boolean)objValue;
            } else {
                log.debug("bind value is null");
            }
        }

        String id = writer.newId(this);

        if(booleanValue) {

            writer.selfClosedElement("input",
                    "type", "checkbox",
                    "class","wCheckbox",
                    "checked","checked",
                    "id", id);

        } else {

            writer.selfClosedElement("input",
                    "type", "checkbox",
                    "class","wCheckbox",
                    "id", id);
        }

        writer.element("label",
                "for",id);
        writer.writeRaw(label);
        writer.end("label");

        writer.registerEvent(id, ScriptEvents.CHANGE, encodedEvent);

        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);
        writer.end("input");
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }
}
