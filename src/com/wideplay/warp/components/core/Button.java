package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.util.TextTools;

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
public class Button implements Renderable, AttributesInjectable {
    private String event;
    private String label = "";
    private Map<String, Object> attribs;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        String encodedEvent = TextTools.EMPTY_STRING;
        if (null != event)
            encodedEvent = event;

        String buttonId = writer.makeIdFor(this);
        writer.elementWithAttrs("input",
                new Object[] {
                        "type", "button",
                        "id", buttonId,
                        "value", label
                },

                ComponentSupport.getTagAttributesExcept(attribs, "type", "id", "value", "onclick")
        );

        writer.registerEvent(buttonId, ScriptEvents.CLICK, encodedEvent, 0);
        
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


    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return attribs;
    }
}
