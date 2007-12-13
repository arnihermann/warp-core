package com.wideplay.warp.components.core;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.components.AttributesInjectable;
import static com.wideplay.warp.components.core.ComponentSupport.getTagAttributesExcept;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.ioc.el.Expressions;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RenderingContext;
import com.wideplay.warp.rendering.RequestBinder;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * Generates a select box from a collection of values.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component
public class SelectBox implements Renderable, AttributesInjectable {
    private String bind;
    private Object items;
    private String label;

    private Map<String, Object> injectableAttributes;

    private final RequestBinder requestBinder;

    @Inject
    public SelectBox(RequestBinder requestBinder) {
        this.requestBinder = requestBinder;
    }

    public void render(RenderingContext context, List<? extends ComponentHandler> nestedComponents) {
        HtmlWriter writer = context.getWriter();

        Object boundItem = null;
        String id = writer.makeIdFor(this);
        
        if (null != bind) {
            //TODO instead of this hack, remember the bind source and target in the conversation, then bind accordingly.
            //TODO fix!!!!!!
            String bindingExpression = requestBinder.createCollectionBindingExpression(null, bind);

            writer.registerInputBinding(id);

            writer.elementWithAttrs("select", getTagAttributesExcept(new Object[] { "id", id, "name", bindingExpression },
                    injectableAttributes, "name"));    //bind as expression
            boundItem = Expressions.evaluate(bind, context.getContextVars());
        }
        else
            writer.element("select", getTagAttributesExcept(Expressions.EMPTY_ARRAY, injectableAttributes));


        
        //obtain the bound object (collection or array)
        Object itemsObject = items;

        //see if it is an iterable
        if (itemsObject instanceof Iterable)
            for (Object item : (Iterable) itemsObject)
                writeOption(item, writer, boundItem, context);
            
        else
            //must be an array
            for (Object item : (Object[])itemsObject)
                writeOption(item,  writer, boundItem, context);

        writer.end("select");
    }

    private void writeOption(Object item, HtmlWriter writer, Object boundItem, RenderingContext context) {
        //the index is basically the hashcode as a string (following Josh Bloch's recommendation and according to the Java collections framework)
        String indexValue = Integer.toString(item.hashCode());

        //bind it as an expression (selecting the user's value from the item list by matching hashcode):
        if (null != bind)
           indexValue = String.valueOf(item.hashCode());

        //resolve label from either set value or use the item itself
        String labelValue = this.label;
        if (null != labelValue)
            labelValue = (String) Expressions.evaluate(this.label, item);
        else
            labelValue = item.toString();

        //write tag out with index and value (matching selected if already bound)
        if (null != boundItem && item.equals(boundItem))
            writer.element("option", "value", indexValue, "selected", "true");
        else
            writer.element("option", "value", indexValue);
        
        writer.writeRaw(labelValue);
        writer.end("option");
    }


    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public void setAttributeNameValuePairs(Map<String, Object> attribs) {
        this.injectableAttributes = attribs;
    }

    public Map<String, Object> getAttributeNameValuePairs() {
        return injectableAttributes;
    }
}
