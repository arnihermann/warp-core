package com.wideplay.warp.internal.pages;

import com.wideplay.warp.rendering.PageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 7, 2007
 * Time: 4:33:59 PM
 *
 *
 */
class UriMatchTreeBuilder {

    //builds an n-nary search tree of maps that can be searched for URI templates by the UriMatcher class
    public void buildAndStore(String uri, PageHandler pageHandler, Map<String, Object> pagesByUriTemplate) {
        //chew up leading '/'
        if ('/' == uri.charAt(0))
            uri = uri.substring(1);
        
        String[] parts = uri.split("[/]");

        Map<String, Object> pages = pagesByUriTemplate;

        //skip the last element as it is the template parameter name
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            //last element store the page handler
            if (i == parts.length - 2) {
                pages.put(part, pageHandler);
            } else {
                Map<String, Object> tempPages = new HashMap<String, Object>();
                pages.put(part, tempPages);
                pages = tempPages;
            }
        }
    }

}
