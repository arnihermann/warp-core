package com.wideplay.warp.internal.pages;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * This interface is only ever implemented by proxies and so has
 * VERY unusual method names to reduce the possibility of collisions.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface PageIdentity {
    String __warpPageIdentity();

    String WARP_PAGE_ID_GETTER = "__warpPageIdentity";
}
