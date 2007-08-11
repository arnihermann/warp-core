package com.wideplay.warp.components.core;

import com.google.inject.Injector;
import com.google.inject.Inject;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.componentry.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RequestBinder;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * Generates a select box from a collection of values.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class SelectBox implements Renderable {
    private String bind;
    private String items;
    private String label;

    private final RequestBinder requestBinder;

    @Inject
    public SelectBox(RequestBinder requestBinder) {
        this.requestBinder = requestBinder;
    }

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        Object boundItem = null;
        if (null != bind) {
            //write special collection binding parameter name
            String bindingExpression = requestBinder.createCollectionBindingExpression(items, bind);

            writer.element("select", "name", bindingExpression);    //bind as expression
            boundItem = BeanUtils.getFromPropertyExpression(bind, page);
        }
        else
            writer.element("select");

        //obtain the bound object
        Object itemsObject = BeanUtils.getFromPropertyExpression(items, page);

        //see if it is an iterable
        if (itemsObject instanceof Iterable)
            for (Object item : (Iterable) itemsObject)
                writeOption(item, writer, boundItem);
            
        else
            //must be an array
            for (Object item : (Object[])itemsObject)
                writeOption(item,  writer, boundItem);

        writer.end("select");
    }

    private void writeOption(Object item, HtmlWriter writer, Object boundItem) {
        //the index is basically the hashcode as a string (following Josh Bloch's recommendation and according to the Java collections framework)
        String indexValue = Integer.toString(item.hashCode());

        //bind it as an expression (selecting the user's value from the item list by matching hashcode):
        if (null != bind)
           indexValue = String.valueOf(item.hashCode());

//            indexValue = bind + " = " + items + ".{? #this.hashCode() == " + indexValue + "}[0]";

        //resolve label from either set value or use the item itself
        String labelValue = this.label;
        if (null != labelValue)
            labelValue = (String) BeanUtils.getFromPropertyExpression(this.label, item);
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

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
