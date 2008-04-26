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
public class WidgetDispatcher {
    private final PageBook book;
    private final Provider<Respond> respondProvider;

    @Inject
    public WidgetDispatcher(PageBook book, Provider<Respond> respondProvider) {
        this.book = book;
        this.respondProvider = respondProvider;
    }

    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final PageBook.Page page = book.get(request.getPathInfo());
        final Respond respond = respondProvider.get();

        //bind request
        //...

        //fire events
        //...

        //render to respond
        page.widget().render(page.instantiate(), respond);
             

        //render buffered "respond" to reponse
        response.getWriter().write(respond.toString());
    }
}
