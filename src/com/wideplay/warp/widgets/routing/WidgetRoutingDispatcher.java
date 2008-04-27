package com.wideplay.warp.widgets.routing;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.binding.RequestBinder;

import javax.servlet.http.HttpServletRequest;

import net.jcip.annotations.Immutable;

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
        fireEvent(request, page, instance);

        //render to respond
        page.widget().render(instance, respond);

        return respond;
    }

    private void fireEvent(HttpServletRequest request, PageBook.Page page, Object instance) {
        final String method = request.getMethod();
        final String pathInfo = request.getPathInfo();
        
        if ("GET".equalsIgnoreCase(method))
            page.doGet(instance, pathInfo);
        else if ("POST".equalsIgnoreCase(method))
            page.doPost(instance, pathInfo);
    }
}
