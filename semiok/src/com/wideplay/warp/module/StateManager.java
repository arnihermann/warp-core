package com.wideplay.warp.module;

import com.wideplay.warp.module.pages.PageClassReflection;

/**
 * Created by IntelliJ IDEA.
 * User: dprasanna
 * Date: 28/03/2007
 * Time: 14:51:21
 * <p/>
 * TODO: Describe me!
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface StateManager {
    /**
     *
     * This method manages fields marked with @Managed in accordance with the following rule:
     *
     * <ul>
     *  <li>First try to see if the property exists in the StateManager's cube (in current scope)</li>
     *  <li>Otherwise load the current value of the property</li>
     *  <li>If none, obtain a new instance (by type) from guice</li>
     * </ul>
     *
     * Then either store the new value into the cube, or restore the cube's value (depending on the above)
     *
     * @param reflection A reflection of the page to inject
     * @param page The object on whom to manage properties
     */
    void injectManaged(PageClassReflection reflection, Object page);

    void extractAndStore(PageClassReflection reflection, Object page);
}
