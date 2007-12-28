package com.wideplay.warp.annotations;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 28, 2007
 * Time: 1:24:23 PM
 *
 * <p>
 * Use this annotation on page objects or Warp-managed types to declare
 * static asset dependencies (such as javascript or css files). Asset are assumed
 * to sit adjacent to the type (class) on which the annotation is found. If no such
 * file is found, Warp defaults to the servlet resource path looking for the literal
 * file name. For example:
 *
 * </p>
 *
 * <pre>
 *  @Asset(uri="/myscript.js", resource="myscript.js")
 *  @Asset(uri="/styles/default.css", resource="default.css")
 *  public class MyPage { .. }
 *
 * </pre>
 *
 * <p>
 *  In this example, myscript.js is searched for in the same package as <code>MyPage</code>.
 * When it is requested by a browser, the loaded asset is served (instead of going down the
 * servlet pipeline). If /styles/default.css is requested, then the resource default.css is
 * served. The URI path has no bearing on where the resource actually sits (in the classpath).
 *
 * Asset URIs are always static. Assets mapping to the same URI will be preferred over a page
 * (and the page mapping ignored).
 *
 * You typically want to use the Asset annotation to register assets that will be packaged
 * in a jar file along with a library of components or pages (for instance). You do *not*
 * need to register assets simply to use them (and shouldn't if they are available in the servlet
 * resource path). Assets registered via the asset annotation indicate to warp to load and serve
 * the given asset. They don't actually get linked on the page or component. 
 *
 * </p>
 *
 * <p>
 * Assets can be any binary file that can be read by an inputstream and written to the response
 * output stream as a byte array. Thus images can be packaged this way.
 *
 * Assets headers should typically be set via a filter in the servlet pipeline. See warp-servlet
 * for a powerful set of tools for filtering the servlet pipeline.
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@BindingAnnotation  //for convenient cross-purpose uses
@Documented
public @interface Asset {
    String uri();
    String resource();
}
