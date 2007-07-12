package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.HtmlWriter;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
class JsSupportUtils {
    static String wrapOnFrameLoadFn(StringBuilder content) {
        //insert content in reverse order at index 0
        content.insert(0, "{");
        content.insert(0, HtmlWriter.ON_FRAME_LOAD_FUNCTION);
        content.insert(0, "window.onload=function ");

        //close func
        content.append("};");

        //append support functions to onFrameLoad
//        content.append(FN_PAGE_EVENT_PUBLISH);

        return content.toString();
    }
    
    static String wrapLinkedScripts(Set<String> linkedScripts) {
        StringBuilder builder = new StringBuilder();
        for (String script : linkedScripts) {
            builder.append("<script type=\"text/javascript\" src=\"");
            builder.append(script);
            builder.append("\"></script>");
        }

        return builder.toString();
    }
}
