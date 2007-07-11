package com.wideplay.warp.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.rendering.RequestBinder;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class Link implements Renderable {
    private String event;
    private String topic;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        String encodedEvent = TextTools.EMPTY_STRING;
        if (null != event)
            encodedEvent = event;

        //write the anchor with a generated id
        String id = writer.newId(this);

        //get topic value from page
        final Object topicValue = BeanUtils.getFromPropertyExpression(topic, page);

        //TODO try and move this off into a javascript remoting module
        writer.element("input", "type", "hidden", RequestBinder.EVENT_TOPIC_PARAMETER_NAME, topicValue);
        writer.element("a", "id", id, "href", "#");

        //register event publication
        writer.registerEvent(id, ScriptEvents.CLICK, encodedEvent);

        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);
        writer.end("a");
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
