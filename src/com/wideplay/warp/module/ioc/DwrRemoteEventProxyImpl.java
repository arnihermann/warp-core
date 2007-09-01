package com.wideplay.warp.module.ioc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.wideplay.warp.annotations.event.PostRender;
import com.wideplay.warp.annotations.event.PreRender;
import com.wideplay.warp.components.core.ComponentSupport;
import static com.wideplay.warp.components.core.ComponentSupport.obtainFrameNestedContent;
import com.wideplay.warp.internal.conversation.ConversationSupport;
import com.wideplay.warp.internal.pages.PageBuilders;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.module.pages.PageClassReflection;
import com.wideplay.warp.rendering.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * On: 27/08/2007
 *
 * TODO factor out commonality with PageHandlerImpl
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class DwrRemoteEventProxyImpl implements RemoteEventProxy {
    private final WarpModuleAssembly assembly;

    @Inject
    public DwrRemoteEventProxyImpl(WarpModuleAssembly assembly) {
        this.assembly = assembly;
    }

    /**
     *
     * @param params A map of parameter names and values to bind to the target page service
     * @return Returns rendered XML result of the page, which is sent to the Dwr javascript engine to do what it wants with
     */
    public String dispatchEvent(Map<String, String> params) {
        String targetURI = params.get(WARP_TARGET_PAGE_URI);

        //locate page for target uri (as specified in the given parameter)
        PageHandler facingPage = assembly.getUserFacingPage(targetURI);
        ComponentHandler rootComponentHandler = facingPage.getRootComponentHandler();

        //TODO add state manager

        //obtain injector and page instance
        Injector injector = assembly.getInjector();
        PageClassReflection reflection = facingPage.getPageClassReflection();
        Object page = injector.getInstance(reflection.getPageClass());

        //locate event id & topic
        String eventId = params.get(RequestBinder.EVENT_PARAMETER_NAME);
        final String topicParam = params.get(RequestBinder.EVENT_TOPIC_PARAMETER_NAME);

        //read topic out of conversation if available
        Object topic = ConversationSupport.retrieveEventTopicAndClear(injector, topicParam);

        //bind request parameters to page
        bindParameters(params, injector, page);


        //fire lifecycle method pre-render (its topic is extracted from the URI)
        reflection.fireEvent(page, PreRender.EVENT_ID, null);

        //fire page event handlers as necessary
        if (null != eventId)
            reflection.fireEvent(page, eventId, topic);

        //render component tree (of everything inside frame component) inside a special htmlwriter
        HtmlWriter htmlWriter = injector.getInstance(Key.get(HtmlWriter.class, DwrResponse.class));
        ComponentSupport.renderMultiple(createContext(htmlWriter, injector, page, reflection),
                obtainFrameNestedContent(rootComponentHandler.getNestedComponents()));

        //fire lifecycle post-render
        reflection.fireEvent(page, PostRender.EVENT_ID, null);

        //return the rendered component tree as a string to dwr
        return htmlWriter.getBuffer();
    }

    private RenderingContext createContext(final HtmlWriter htmlWriter, final Injector injector, final Object page,
                                           final PageClassReflection reflection) {
        
        return PageBuilders.newRenderingContext(htmlWriter, injector, reflection, page);
    }

    private void bindParameters(Map<String, String> params, Injector injector, Object page) {
        if (null != params.get(RequestBinder.EVENT_PARAMETER_NAME))
            injector.getInstance(RequestBinder.class).bindBeanFromMap(page, params);
    }
}
