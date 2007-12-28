package com.wideplay.warp.rendering;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 4:18:05 PM
 *
 * Filters any rendered text just before it goes out to the response.
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public interface ContentFilter {
    String filter(String content);
}
