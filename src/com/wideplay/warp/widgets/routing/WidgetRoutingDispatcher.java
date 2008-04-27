package com.wideplay.warp.widgets.routing;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wideplay.warp.widgets.Respond;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class WidgetRoutingDispatcher implements RoutingDispatcher {
    private final PageBook book;
    private final Provider<Respond> respondProvider;

    @Inject
    public WidgetRoutingDispatcher(PageBook book, Provider<Respond> respondProvider) {
        this.book = book;
        this.respondProvider = respondProvider;
    }

    public Respond dispatch(HttpServletRequest request) {
        final PageBook.Page page = book.get(request.getPathInfo());

        //could not dispatch as there was no match
        if (null == page)
            return null;

        final Respond respond = respondProvider.get();

        //bind request
        //...

        //fire events
        //...

        //render to respond
        page.widget().render(page.instantiate(), respond);

        return respond;
    }
}
