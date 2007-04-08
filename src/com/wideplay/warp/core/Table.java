package com.wideplay.warp.core;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.components.ClassReflectionCache;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.util.beans.BeanUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class Table implements Renderable {
    private String items;
    private boolean asGrid = false;

    private static final String GRID_CONVERSION_FUNCTION = PageBuilders.loadResource(Table.class, "Grid.js");

    @Inject private ClassReflectionCache classCache;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        String id = writer.newId(this);
        writer.element("table", "id", id);

        //obtain the bound object
        Object itemsObject = BeanUtils.getFromPropertyExpression(items, page);

        //see if it is an iterable
        if (itemsObject instanceof Iterable) {
            Map<String, String> propertiesAndLabels = null;
            Iterator iter = ((Iterable) itemsObject).iterator();

            boolean isFirst = true;
            while(iter.hasNext()) {
                Object item = iter.next();

                if (isFirst) {
                    propertiesAndLabels = classCache.getPropertyLabelMap(item);
                    writeHeader(writer, propertiesAndLabels);
                    isFirst = false;
                    writer.element("tbody");
                }

                writeRow(item, writer, propertiesAndLabels);
            }

            writer.end("tbody");

        } else {    //is an array
            Map<String, String> propertiesAndLabels = null;
            Object[] array = ((Object[]) itemsObject);

            for (int i = 0 ; i < array.length; i++) {
                Object item = array[i];

                if (0 == i) {
                    propertiesAndLabels = classCache.getPropertyLabelMap(item);
                    writeHeader(writer, propertiesAndLabels);
                    writer.element("tbody");
                }

                writeRow(item, writer, propertiesAndLabels);
            }
            writer.end("tbody");
        }
        writer.end("table");


        if (asGrid) {
            //convert to grid
            writer.registerScriptLibrary(CoreScriptLibraries.YUI_UTILITIES);
            writer.registerScriptLibrary(CoreScriptLibraries.EXT_YUI_ADAPTER);
            writer.registerScriptLibrary(CoreScriptLibraries.EXT_ALL);

            //write out the function TODO minify or externalize
            writer.writeToOnLoad(GRID_CONVERSION_FUNCTION);

            //write out the conversion call
            writer.writeToOnLoad(
                    "var grid = new Ext.grid.TableGrid(\"");
            writer.writeToOnLoad(id);
            writer.writeToOnLoad("\");\n" +
                    "grid.render();");
        }

    }

    private void writeHeader(HtmlWriter writer, Map<String, String> propertiesAndLabels) {
        //write out header
        writer.element("thead");
        writer.element("tr");
        for (String label : propertiesAndLabels.values()) {
            writer.element("th");
            writer.writeRaw(label);
            writer.end("th");
        }
        writer.end("tr");
        writer.end("thead");
    }

    private void writeRow(Object item, HtmlWriter writer, Map<String, String> propertiesAndLabels) {
        writer.element("tr");
        for (String property : propertiesAndLabels.keySet()) {
            writer.element("td");
            writer.writeRaw(BeanUtils.getFromPropertyExpression(property, item).toString());
            writer.end("td");
        }
        writer.end("tr");        
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public void setAsGrid(boolean asGrid) {
        this.asGrid = asGrid;
    }
}
