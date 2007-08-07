package com.wideplay.warp.internal.conversation;

import com.google.inject.servlet.SessionScoped;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 4/07/2007
 *
 * An internal conversation artifact that is used to manage sequences of page navigation
 * but *not* @Managed properties.
 *
 * This is primarily intended to store internal stuff in pseudo flash-scope between consecutive
 * requests to a page. For example, event "subjects" or other temporary metadata. This is different
 * from a "user" conversation which may be scoped differently (i.e. wider than session).
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
@SessionScoped
public class InternalConversation {

    //conversation store (store conversational artifacts in here across a sequence of requests)
    private final Map<Integer, Object> store = new HashMap<Integer, Object>();

    public void remember(Object topic) {
        store.put(topic.hashCode(), topic);
    }

    public Object recall(Integer hashCode) {
        return store.get(hashCode);
    }

    public void forgetAll() {
        store.clear();
    }
}
