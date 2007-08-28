package com.wideplay.warp.rendering;

import com.wideplay.warp.components.AttributesInjectable;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 29/03/2007
 * Time: 12:21:58
 * <p/>
 * TODO: Describe me!
 *
 * @author dprasanna
 * @since 1.0
 */
public interface HtmlWriter {
    //function names and placeholders
    String ON_FRAME_LOAD_PLACEHOLDER = "__WARP_OnFrameLoadPlaceHolder__";
    String LINKED_SCRIPTS_PLACEHOLDER = "__WARP_LinkedScriptsPlaceHolder__";
    String FRAME_GLOBAL_FORM_NAME = "__warpForm";
    String ON_FRAME_LOAD_FUNCTION = "fnOnFrameLoad()";

    /**
     * This method tells HtmlWriter that a particular property is to be watched as an incoming request
     * parameter. This is typically used when "gathering" properties on a page or viewport to post back
     * via javascript for example.
     *
     * The default implementation does nothing (since pages use the &lt;form&gt; tag for normal posts anyway)
     *
     * @param id Id of the element/tag with the bound property/request parameter
     */
    void registerInputBinding(String id);

    void registerScriptLibrary(ScriptLibrary library);

    void registerEvent(String elementName, ScriptEvents event, String annotation, int topicId);//write raw text to the body load js func

    void writeToOnLoad(String text);

    String makeIdFor(AttributesInjectable object);

    //convenience varargs method

    void element(String name, Object...nameValuePairs);

    void elementWithAttrs(String name, Object[]... nameValuePairs);

    void writeRaw(String text);

    void end(String name);

    void selfClosedElement(String name, Object...nameValuePairs);
    void selfClosedElementWithAttrs(String name, Object[] nameValuePairs);

    String getBuffer();

    void registerAsyncEvent(String id, ScriptEvents click, String encodedEvent, int topicId, String[] viewports);

    String newId(Object object);
}
