package com.wideplay.warp.internal.pages;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.wideplay.warp.annotations.event.PostRender;
import com.wideplay.warp.annotations.event.PreRender;
import com.wideplay.warp.internal.conversation.ConversationSupport;
import com.wideplay.warp.module.StateManager;
import com.wideplay.warp.module.WarpModuleAssembly;
import com.wideplay.warp.rendering.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
class PageHandlerImpl implements PageHandler {
    private final PageClassReflectionImpl reflection;
    private final String uri;
    private final ComponentHandler rootComponentHandler;

    public PageHandlerImpl(String uri, PageClassReflectionImpl reflection, ComponentHandler root) {
        this.uri = uri;
        this.reflection = reflection;
        this.rootComponentHandler = root;
    }


    public Object handleRequest(HttpServletRequest request, HttpServletResponse response, Injector injector,
                                Object page, String uriPart) {
        //obtain the page object from guice (makes sure it is properly injected) ONLY if it is not provided
        if (null == page)
            page = injector.getInstance(reflection.getPageClass());

        //locate event id & topic
        String eventId = request.getParameter(RequestBinder.EVENT_PARAMETER_NAME);
        final String topicParam = request.getParameter(RequestBinder.EVENT_TOPIC_PARAMETER_NAME);

        //read topic out of conversation if available
        Object topic = ConversationSupport.retrieveEventTopicAndClear(injector, topicParam);

        //place persistent fields back into the page
        StateManager stateManager = injector.getInstance(StateManager.class);
        stateManager.injectManaged(reflection, page); //todo move out to InjectionSupport

        //map request parameters back into page object (only on events)
        bindRequestParameters(request, injector, page);

        Object forwardPage;

        //fire lifecycle method pre-render (its topic is extracted from the URI)
        forwardPage = reflection.fireEvent(page, PreRender.EVENT_ID, uriPart);
        if (null != forwardPage)
            return forwardPage;

        //fire page event handlers as necessary
        if (null != eventId)
            forwardPage = reflection.fireEvent(page, eventId, topic);

        //test if we need to forward to a different page
        if (null != forwardPage)
            return forwardPage;

        //render template with the page as its model
        renderPage(injector, page, response);

        //fire lifecycle post-render
        forwardPage = reflection.fireEvent(page, PostRender.EVENT_ID, topic);

        //ok now reabsorb managed properties into the statemanager
        stateManager.extractAndStore(reflection, page);

        //everything was ok, so return forwardpage (if null it stays on same)
        return forwardPage;
    }

    private void renderPage(Injector injector, Object page, HttpServletResponse response) {
        //write response to a new HtmlWriter using the component handler tree
        HtmlWriter htmlWriter = injector.getInstance(Key.get(HtmlWriter.class, JsFrame.class));
        rootComponentHandler.handleRender(new RenderingContextImpl(htmlWriter, injector, reflection, page));

        //write buffered output to the response stream
        try {
            response.getWriter().write(htmlWriter.getBuffer());
        } catch (IOException e) {
            throw new PageRenderException("Error obtaining the response writer while rendering page at: "
                    + injector.getInstance(WarpModuleAssembly.class).resolvePageURI(page), e);
        }
    }

    

    @SuppressWarnings("unchecked")
    private void bindRequestParameters(HttpServletRequest request, Injector injector, Object page) {
        if (null != request.getParameter(RequestBinder.EVENT_PARAMETER_NAME))
            injector.getInstance(RequestBinder.class).bindObject(page, request.getParameterMap());
    }


    public Class<?> getPageClass() {
        return reflection.getPageClass();
    }

    public PageClassReflectionImpl getPageClassReflection() {
        return reflection;
    }


    public ComponentHandler getRootComponentHandler() {
        return rootComponentHandler;
    }
}
