package com.wideplay.warp.components.core;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.components.AttributesInjectable;
import com.wideplay.warp.internal.conversation.InternalConversation;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.rendering.ScriptEvents;
import com.wideplay.warp.util.TextTools;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 *
 * Generates an on-page link, that when clicked, triggers an event to the page object.
 *
 * Events can be subscribed to with the @OnEvent annotation on a method. Specifying a w:event attribute
 * will fire events to a custom event annotation. Example:
 *
 * <a w:component="link" w:event="@Add">add!</a>
 *
 * will fire events *only* to the following event handler (and similar ones):
 *
 * <pre>
 * @OnEvent @Add
 * public void myEventHandler() {
 *
 * }
 * </pre>
 *
 * Use topics to specify an argument to your event handler. A topic maybe any property from the page object
 * or a suitable MVEL expression. Topics are useful inside repeaters where you want to specify what object
 * was clicked.
 *
 * <span w:component="repeater" w:items="myCollection">
 *      <a w:component="link" w:topic="this">add!</a>
 * </span>
 *
 * Then subscribe to it like so:
 *
 * <pre>
 * @OnEvent
 * public void onSelect(Object o) {
 *     //link for "o" was clicked, do something...
 * }
 * </pre>
 *
 * Note that the event handler can be declared with any argument type (not just java.lang.Object) so long
 * as the topic object can be coerced into that type (example: String can be coerced into Serializable).
 *
 * An event handler without a topic *must* take no arguments. An event handler with a topic *must* declare
 * one argument exactly. 
 *
 */
@Component
public class Link implements Renderable, AttributesInjectable {
    private String event;
    private Object topic;
    private String viewports;
    private final InternalConversation conversation;
    private Map<String, Object> attribs;

    @Inject
    public Link(InternalConversation conversation) {
        this.conversation = conversation;
    }

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();
        String encodedEvent = TextTools.EMPTY_STRING;
        if (null != event)
            encodedEvent = event;

        //write the anchor with a generated id
        String id = writer.makeIdFor(this);

        //manage event topics via the internal conversation (tracker of user's behavior across requests)
        int topicId = 0;
        if (null != topic) {
            //value topic value from page
            final Object topicValue = topic;
            topicId = topicValue.hashCode();

            //store it into the internal conversation for later retrieval if necessary...
            conversation.remember(topicValue);
        }

        writer.elementWithAttrs("a", ComponentSupport.getTagAttributesExcept(new Object[] { "id", id, "href", "#"},
                attribs, "id", "href", "onclick"));

        //register event publication
        if (null != viewports)
            writer.registerAsyncEvent(id, ScriptEvents.CLICK, encodedEvent, topicId, TextTools.commaSeparatorRegexSplit(viewports));
        else
            writer.registerEvent(id, ScriptEvents.CLICK, encodedEvent, topicId);

        ComponentSupport.renderMultiple(context, nestedComponents);
        writer.end("a");
    }


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setTopic(Object topic) {
        this.topic = topic;
    }

    public void setViewports(String viewports) {
        this.viewports = viewports;
    }

    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.attribs = attribs;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return attribs;
    }
}
