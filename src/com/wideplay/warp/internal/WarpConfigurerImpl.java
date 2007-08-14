package com.wideplay.warp.internal;

import com.google.inject.Module;
import com.google.inject.Key;
import com.wideplay.warp.Warp;
import com.wideplay.warp.StartupListener;
import com.wideplay.warp.module.WarpConfiguration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: Mar 24, 2007 10:50:28 PM
 *
 * @author Dhanji R. Prasanna
 */
class WarpConfigurerImpl implements Warp, WarpConfiguration {
    private final List<Module> guiceModules = new LinkedList<Module>();
    private final List<Key<? extends StartupListener>> startupListeners = new LinkedList<Key<? extends StartupListener>>();

    //configurable options
    private String urlEncodingScheme = "UTF-8";

    public WarpConfigurerImpl(Module... modules) {
        Collections.addAll(guiceModules, modules);
    }

    public void install(Module guiceModule) {
        guiceModules.add(guiceModule);
    }


    public void addStartupListener(Class<? extends StartupListener> startupListener) {
        startupListeners.add(Key.get(startupListener));
    }


    public void addStartupListener(Key<? extends StartupListener> startupListener) {
        startupListeners.add(startupListener);
    }

    public List<Module> getGuiceModules() {
        return guiceModules;
    }

    public List<Key<? extends StartupListener>> getStartupListeners() {
        return startupListeners;
    }


    public String getUrlEncoding() {
        return urlEncodingScheme;
    }

    public void setUrlEncoding(String urlEncodingScheme) {
        this.urlEncodingScheme = urlEncodingScheme;
    }
}
