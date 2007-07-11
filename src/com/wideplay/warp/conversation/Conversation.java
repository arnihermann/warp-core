package com.wideplay.warp.conversation;

import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * On: 4/07/2007
 *
 * An internal conversation artifact that is used to manage sequences of page navigation
 * but *not* @Managed properties.
 *
 * This is primarily intended to store internal stuff in pseudo flash-scope between consecutive
 * requests to a page. For example, event "subjects" or other temporary metadata.
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class Conversation {

    //conversation store (store conversational artifacts in here across a flow)
    private final Map<String, Object> store = new HashMap<String, Object>();

    
}
