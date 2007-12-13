package com.wideplay.warp.module;

/**
 * Created by IntelliJ IDEA.
 * User: dhanji
 * Date: Aug 13, 2007
 * Time: 9:47:35 AM
 *
 * Represents an immutable runtime instance of the current set of configured options for Warp.
 *
 * Basically, anything that a user configures in WarpModule by invoking Warp.xxx() methods is available
 * from this interface. It is intended primarily for components to inspect the runtime config options,
 * but may also be used for debugging and the like.
 *
 * This interface is expected go grow linearly in parallel to the Warp interface.
 *
 */
public interface WarpConfiguration {
    String getUrlEncoding();
}
