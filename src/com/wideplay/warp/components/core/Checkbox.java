package com.wideplay.warp.components.core;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.module.ioc.el.Expressions;

import java.util.List;


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

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        boolean booleanValue=false;

        if (null != bind) {
            Object objValue = Expressions.evaluate(bind, context.getContextVars());

            if(null != objValue) {
                if (objValue instanceof Boolean)
                    booleanValue = (Boolean)objValue;
            }
        }

        String id = writer.newId(this);
        writer.registerInputBinding(id);

        writer.selfClosedElement("input",
                "type","hidden",
                "name",bind,
                "value","false");

        if(booleanValue) {

            writer.selfClosedElement("input",
                    "type", "checkbox",
                    "class","wCheckbox",
                    "checked","checked",
                    "value","true",
                    "name",bind,
                    "id", id);

        } else {

            writer.selfClosedElement("input",
                    "type", "checkbox",
                    "class","wCheckbox",
                    "value","true",
                    "name",bind,
                    "id", id);
        }

        writer.element("label",
                "for",id);
        writer.writeRaw(label);
        writer.end("label");

        if (null != event)
            writer.registerEvent(id, ScriptEvents.CHANGE, event, 0);


        ComponentSupport.renderMultiple(context, nestedComponents);
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
