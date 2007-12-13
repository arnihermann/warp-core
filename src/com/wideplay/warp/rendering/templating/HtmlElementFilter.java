package com.wideplay.warp.rendering.templating;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 9, 2007
 * Time: 3:35:53 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public interface HtmlElementFilter {
    /**
     *
     * @param element The element being written (i.e. filter this)
     * @return Returns a filtered version of the element. Return null to hide the element
     *  altogether. Return the provided element to render normally (i.e. with no changes).
     */
    HtmlElement filter(HtmlElement element);
}
