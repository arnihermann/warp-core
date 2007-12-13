package com.wideplay.warp;

/**
 * Created with IntelliJ IDEA.
 * On: 4/07/2007
 *
 * Register any class that implements this interface to receive a callback
 * after warp is initialized, and to perform one-time startup work in your app.
 *
 * Your class is obtained via guice, so any injections will be honored.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public interface StartupListener {

    /**
     * Invoked by the Warp startup cycle for the application to perform any one-time initialization
     * work (typical usage scenario is starting up the PersistenceService).
     */
    void onStartup();
}
