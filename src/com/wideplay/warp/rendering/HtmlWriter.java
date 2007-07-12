package com.wideplay.warp.rendering;

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

    void registerScriptLibrary(ScriptLibrary library);

    void registerEvent(String elementName, ScriptEvents event, String annotation, int topicId);//write raw text to the body load js func

    void writeToOnLoad(String text);

    String newId(Object object);//convenience varargs method

    void element(String name, Object...nameValuePairs);

    void elementWithAttrs(String name, Object[] nameValuePairs);

    void writeRaw(String text);

    void end(String name);

    void selfClosedElement(String name, Object...nameValuePairs);
    void selfClosedElementWithAttrs(String name, Object[] nameValuePairs);

    String getBuffer();
}
