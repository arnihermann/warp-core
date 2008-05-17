package com.wideplay.warp.widgets.routing;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.binding.RequestBinder;
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

    @Inject
    public WidgetRoutingDispatcher(PageBook book, RequestBinder binder, Provider<Respond> respondProvider) {
        this.book = book;
        this.binder = binder;
        this.respondProvider = respondProvider;
    }

    public Respond dispatch(HttpServletRequest request) {
        final PageBook.Page page = book.get(request.getPathInfo());

        //could not dispatch as there was no match
        if (null == page)
            return null;

        final Respond respond = respondProvider.get();
        final Object instance = page.instantiate();

        //bind request
        binder.bind(request, instance);

        //fire get/post events
        final Object redirect = fireEvent(request, page, instance);

        //render to respond
        if (null != redirect)
            respond.redirect((String)redirect);
        else
            page.widget().render(instance, respond);

        return respond;
    }

    private Object fireEvent(HttpServletRequest request, PageBook.Page page, Object instance) {
        final String method = request.getMethod();
        final String pathInfo = request.getPathInfo();

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
