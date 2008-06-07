package com.wideplay.warp.widgets.routing;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.binding.RequestBinder;
import com.wideplay.warp.widgets.resources.ResourcesService;
import net.jcip.annotations.Immutable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @Singleton
class WidgetRoutingDispatcher implements RoutingDispatcher {
    private final PageBook book;
    private final RequestBinder binder;
    private final Provider<Respond> respondProvider;
    private final ResourcesService resourcesService;

    @Inject
    public WidgetRoutingDispatcher(PageBook book, RequestBinder binder, Provider<Respond> respondProvider,
                                   ResourcesService resourcesService) {
        this.book = book;
        this.binder = binder;
        this.respondProvider = respondProvider;
        this.resourcesService = resourcesService;
    }

    public Respond dispatch(HttpServletRequest request) {
        String uri = getPathInfo(request);

        //normalize to home
//        if (null == uri)
//            uri = "/";

        //first try dispatching as a resource service
        Respond respond = resourcesService.serve(uri);

        if (null != respond)
            return respond;


        //otherwise try to dispatch as a widget/page
        final PageBook.Page page = book.get(uri);

        //could not dispatch as there was no match
        if (null == page)
            return null;

        respond = respondProvider.get();
        final Object instance = page.instantiate();

        //fire events and render reponders
        bindAndRespond(request, page, respond, instance);

        return respond;
    }

    private String getPathInfo(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    private void bindAndRespond(HttpServletRequest request, PageBook.Page page, Respond respond, Object instance) {
        //bind request
        binder.bind(request, instance);

        //fire get/post events
        final Object redirect = fireEvent(request, page, instance);

        //render to respond
        if (null != redirect)
            respond.redirect((String)redirect);
        else
            page.widget().render(instance, respond);
    }

    private Object fireEvent(HttpServletRequest request, PageBook.Page page, Object instance) {
        final String method = request.getMethod();
        final String pathInfo = getPathInfo(request);

        Object redirect = null;
        if ("GET".equalsIgnoreCase(method))
            //noinspection unchecked
            redirect = page.doGet(instance, pathInfo, request.getParameterMap());
        else if ("POST".equalsIgnoreCase(method))
            //noinspection unchecked
            redirect = page.doPost(instance, pathInfo, request.getParameterMap());

        return redirect;
    }
}
