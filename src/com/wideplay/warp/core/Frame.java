package com.wideplay.warp.core;

import com.google.inject.Injector;
import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.module.components.Renderable;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.ComponentHandler;
import com.wideplay.warp.rendering.HtmlWriter;
import com.wideplay.warp.rendering.RequestBinder;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@Component
public class Frame implements Renderable {
    private String title;
    private String css;
    private Map<String, String> metaInfo;

    public void render(HtmlWriter writer, List<? extends ComponentHandler> nestedComponents, Injector injector, PageClassReflection reflection, Object page) {
        writer.element("html");
        writer.element("head");
        
        writer.element("title");
        writer.writeRaw(title);
        writer.end("title");

        if (null != css)
            writeCss(writer);

        //write some script framing
        writer.writeRaw(HtmlWriter.LINKED_SCRIPTS_PLACEHOLDER);

        //register common script libraries needed by all components
//        writer.registerScriptLibrary(CoreScriptLibraries.YUI_MIN);
//        writer.registerScriptLibrary(CoreScriptLibraries.YUI_EVENT_MIN);

        //write a placeholder for the onFrameLoad function
        writer.element("script", "type", "text/javascript");
        writer.writeRaw(HtmlWriter.ON_FRAME_LOAD_PLACEHOLDER);
        writer.end("script");

        writer.end("head");

        //write body
        writer.element("body", "onLoad", HtmlWriter.ON_FRAME_LOAD_FUNCTION);
        writer.element("form", "name", HtmlWriter.FRAME_GLOBAL_FORM_NAME, "method", "post");

        //event ids are stored in a hidden field TODO make this nicer somehow (pull into HtmlWriter?)        
        writer.selfClosedElementWithAttrs("input", new Object[] { "type", "hidden", "name", RequestBinder.EVENT_PARAMETER_NAME });

        //dispatch to children
        ComponentSupport.renderMultiple(writer, nestedComponents, injector, reflection, page);

        writer.end("form");
        writer.end("body");
        writer.end("html");
    }

    private void writeCss(HtmlWriter writer) {
        writer.element("style", "type", "text/css");
        writer.writeRaw("@import \"");
        writer.writeRaw(css);
        writer.writeRaw("\";");
        writer.end("style");
    }


    //getters/setters------------
    public Map<String, String> getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(Map<String, String> metaInfo) {
        this.metaInfo = metaInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }
}
