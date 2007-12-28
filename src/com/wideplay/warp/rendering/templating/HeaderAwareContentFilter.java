package com.wideplay.warp.rendering.templating;

import com.wideplay.warp.rendering.ContentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 4:16:48 PM
 *
 * This filter removes duplicate script and style declarations from an html header.
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public final class HeaderAwareContentFilter implements ContentFilter {
    public String filter(String content) {
        return content;
    }
}
