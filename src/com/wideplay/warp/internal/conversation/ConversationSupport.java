package com.wideplay.warp.internal.conversation;

import com.google.inject.Injector;
import com.wideplay.warp.util.TextTools;

/**
 * Created with IntelliJ IDEA.
 * On: 28/08/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class ConversationSupport {
    /**
     *
     * A utility method to help work with topics in the conversation
     */
    public static Object retrieveEventTopicAndClear(Injector injector, String topicParam) {
        Object topic = null;

        final InternalConversation conversation = injector.getInstance(InternalConversation.class);
        if (null != topicParam) {
            if (!TextTools.isEmptyString(topicParam))
                topic = conversation.recall(Integer.parseInt(topicParam));
        }

        //clear out internal monologue!!!
        conversation.forgetAll();

        return topic;
    }
}
