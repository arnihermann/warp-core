package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.HtmlWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * On: 24/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class JsSupportUtils {
    public static final String DWR_EVENT_DISPATCH_FUNC = "dwrEventDispatch.js";
    private static final Map<String, String> SCRIPTS = Collections.synchronizedMap(new HashMap<String, String>());

    static String wrapOnFrameLoadFn(StringBuilder content) {
        //insert content in reverse order at index 0
        content.insert(0, "{");
        content.insert(0, HtmlWriter.ON_FRAME_LOAD_FUNCTION);
        content.insert(0, "function ");

        //close func
        content.append("};");

        //append support functions to onFrameLoad
//        content.append(FN_PAGE_EVENT_PUBLISH);

        return content.toString();
    }
    
    static String wrapLinkedScripts(Set<String> linkedScripts, String contextPath) {
        StringBuilder builder = new StringBuilder();
        for (String script : linkedScripts) {
            builder.append("<script type=\"text/javascript\" src=\"");

            //add context path as necessary
            builder.append(contextPath);
            if (!script.startsWith("/"))
                builder.append("/");

            builder.append(script);
            builder.append("\"></script>");
        }

        return builder.toString();
    }

    static String getScriptTemplate(String name) {
        String scriptTemplate = SCRIPTS.get(name);
        if (null == scriptTemplate) {
            //try to load it
            try {
                StringBuilder script = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(JsSupportUtils.class.getResourceAsStream(name)));

                while(bufferedReader.ready()) {
                    script.append(bufferedReader.readLine());
                }

                //cache it
                scriptTemplate = script.toString();
                SCRIPTS.put(name, scriptTemplate);

            } catch (IOException e) {
                throw new MissingResourceException("Cannot find resource file: " + name, JsSupportUtils.class.getName(), name);
            }
        }

        return scriptTemplate;
    }
}
