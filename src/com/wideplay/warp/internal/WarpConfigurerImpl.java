package com.wideplay.warp.internal;

import com.google.inject.Key;
import com.google.inject.Module;
import com.wideplay.warp.ShutdownListener;
import com.wideplay.warp.StartupListener;
import com.wideplay.warp.Warp;
import com.wideplay.warp.module.WarpConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:50:28 PM
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 */
class WarpConfigurerImpl implements Warp, WarpConfiguration {
    private final List<Module> guiceModules = new LinkedList<Module>();
    private final List<Key<? extends StartupListener>> startupListeners = new LinkedList<Key<? extends StartupListener>>();
    private final List<Key<? extends ShutdownListener>> shutdownListeners = new LinkedList<Key<? extends ShutdownListener>>();
    private boolean disableDwr = false;

    //configurable options
    private String urlEncodingScheme = "UTF-8";

    public WarpConfigurerImpl(Module... modules) {
        Collections.addAll(guiceModules, modules);
    }

    public void install(Module...modules) {
        guiceModules.addAll(Arrays.asList(modules));
    }


    public void addStartupListener(Class<? extends StartupListener> startupListener) {
        startupListeners.add(Key.get(startupListener));
    }


    public void addStartupListener(Key<? extends StartupListener> startupListener) {
        startupListeners.add(startupListener);
    }

    public void addShutdownListener(Class<? extends ShutdownListener> shutdownListener) {
        shutdownListeners.add(Key.get(shutdownListener));
    }

    public void addShutdownListener(Key<? extends ShutdownListener> shutdownListener) {
        shutdownListeners.add(shutdownListener);
    }

    public List<Module> getGuiceModules() {
        return guiceModules;
    }

    public List<Key<? extends StartupListener>> getStartupListeners() {
        return startupListeners;
    }

    public void disableDwr(boolean disable) {
        this.disableDwr = disable;
    }

    public boolean isDisableDwr() {
        return disableDwr;
    }

    public String getUrlEncoding() {
        return urlEncodingScheme;
    }

    public void setUrlEncoding(String urlEncodingScheme) {
        this.urlEncodingScheme = urlEncodingScheme;
    }

    public List<Key<? extends ShutdownListener>> getShutdownListeners() {
        return shutdownListeners;
    }

}
