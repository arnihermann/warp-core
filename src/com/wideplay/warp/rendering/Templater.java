package com.wideplay.warp.rendering;

import com.google.inject.ImplementedBy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 13, 2007
 * Time: 10:10:39 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
@ImplementedBy(StandardHtmlTemplater.class)
public interface Templater {
    boolean process(HttpServletRequest request, HttpServletResponse response);
}
