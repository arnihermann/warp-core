package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.util.Strings;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.LinkedHashMap;

import net.jcip.annotations.Immutable;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @Singleton
class EmbedWidget implements RenderableWidget {
    private final Map<String, String> bindExpressions;
    private final Evaluator evaluator;
    private final PageBook pageBook;
    private final String targetPage;

    public EmbedWidget(String expression, Evaluator evaluator, PageBook pageBook, String targetPage) {

        this.evaluator = evaluator;
        this.pageBook = pageBook;
        this.targetPage = targetPage.toLowerCase();

        //parse expression list
        this.bindExpressions = toBindMap(expression);
    }

    //converts comma-separated name/value pairs into expression/variable bindings
    private static Map<String, String> toBindMap(String expression) {
        if (Strings.empty(expression))
            return Collections.emptyMap();

        String[] pairs = expression.split(",");

        //nice to preserve insertion order
        final Map<String, String> map = new LinkedHashMap<String, String>();
        for (String pair : pairs) {
            final String[] nameAndValue = pair.split("=");

            //do some validation
            if (nameAndValue.length != 2)
                throw new IllegalArgumentException("Invalid parameter binding format: " + pair);

            Strings.nonEmpty(nameAndValue[0], "Cannot have an empty left hand side target parameter: " + pair);
            Strings.nonEmpty(nameAndValue[1], "Must provide a non-empty right hand side expression" + pair);

            map.put(nameAndValue[0].trim(), nameAndValue[1].trim());
        }

        return Collections.unmodifiableMap(map);
    }

    public void render(Object bound, Respond respond) {
        final PageBook.Page page = pageBook.forName(targetPage);

        //create an instance of the embedded page
        final Object pageObject = page.instantiate();

        //bind parameters to it as necessary
        for (Map.Entry<String, String> entry : bindExpressions.entrySet()) {
            evaluator.write(entry.getKey(), pageObject, evaluator.evaluate(entry.getValue(), bound));
        }

        //chain to embedded page (widget)
        page.widget().render(pageObject, respond);
    }
}
