package com.wideplay.warp.module;

import com.wideplay.warp.rendering.PageHandler;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 7, 2007
 * Time: 3:55:34 PM
 */
class PathDelimitingUriMatcher implements UriMatcher {

    public MatchTuple extractMatch(String uri, Map<String, Object> uris) {

        //chew up leading "/"
        if ('/' == uri.charAt(0))
            uri = uri.substring(1);

        //cannot match "/" context (we dont support directly mapping there)
        if (uri.length() == 0)
            return null;

        //chew up trailing "/"
        if ('/' == uri.charAt(uri.length() - 1))
            uri = uri.substring(0, uri.length() - 1);

        //break up uri in 2 component parts
        return matchUri(uri, uris);
    }

    private MatchTuple matchUri(String uri, Map<String, Object> uris) {
        String[] parts = uri.split("[/]", 2);

        Object uriMarker = uris.get(parts[0]);
        if (null != uriMarker) {

            //test if marker reached (i.e. page handler) with no extract
            if (parts.length == 1) {
                if (uriMarker instanceof PageHandler)
                    return MatchTuple.tuple(null, (PageHandler) uriMarker);
                else
                    return null;    //no match--the URI was shorter than the template
            }

            //test if marker is reached with extract
            if ( (uriMarker instanceof PageHandler)) {

                //end of recursion branch
                return MatchTuple.tuple(parts[1], (PageHandler) uriMarker);

            } else {
                //recurse
                @SuppressWarnings("unchecked")
                final Map<String, Object> uriMarkerMap = (Map<String, Object>) uriMarker;
                
                return matchUri(parts[1], uriMarkerMap);
            }

        }


        //error in URI grammar or no template registered
        return null;
    }

}
