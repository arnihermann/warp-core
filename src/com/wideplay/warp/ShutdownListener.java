package com.wideplay.warp;

/**
 * Created with IntelliJ IDEA.
 * On: 19/08/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public interface ShutdownListener {
    /**
     * Invoked when the WarpFilter is shutting down (do any cleanup work here such as
     * closing db connection pools)
     */
    void onShutdown();
}
