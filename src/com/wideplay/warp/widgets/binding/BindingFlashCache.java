package com.wideplay.warp.widgets.binding;

import com.wideplay.warp.servlet.FlashScoped;
import com.wideplay.warp.servlet.SessionScoped;
import net.jcip.annotations.NotThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 *
 * Used to store binding (or forwarding) information between successive requests.
 */
@SessionScoped @NotThreadSafe
class BindingFlashCache implements FlashCache {
    private final Map<String, Object> cache = new HashMap<String, Object>();

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) cache.get(key);
    }

    public <T> void put(String key, T t) {
        cache.put(key, t);
    }
}
