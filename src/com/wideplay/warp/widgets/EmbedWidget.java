package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.routing.PageBook;
import com.wideplay.warp.util.Strings;
import com.google.inject.Singleton;

import java.util.Map;
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
        final EmbeddedRespond embed = new EmbeddedRespond();
        page.widget().render(pageObject, embed);

        //extract and write embedded response to enclosing page's respond
        respond.writeToHead(embed.toHeadString());
        respond.write(embed.toString());
    }

    
    static class EmbeddedRespond extends StringBuilderRespond {
        private static final String HEAD_BEGIN = "<head";
        private static final String HEAD_END = "</head>";
        private static final String BODY_BEGIN = "<body";
        private static final String BODY_END = "</body>";

        //memo fields
        private String head;
        private String body;
        private static final char NOT_IN_QUOTE = '\0';

        public String toHeadString() {
            if (null == head) {
                //extract and store
                extract(super.toString());
            }

            return head;
        }

        @Override
        public String toString() {
            if (null == body) {
                extract(super.toString());
            }

            return body;
        }

        //state machine extracts <head> and <body> tag content separately
        private void extract(String htmlDoc) {
            int headEnd = extractHead(htmlDoc);

            //now do the body...
            int bodyStart = htmlDoc.indexOf(BODY_BEGIN, headEnd) + BODY_BEGIN.length();

            //scan for end of the <body> start tag (beginning of body content)
            char quote = NOT_IN_QUOTE;
            for (int body = bodyStart; body < htmlDoc.length(); body++) {
                final char c = htmlDoc.charAt(body);
                if (isQuoteChar(c)) {
                    if (quote == NOT_IN_QUOTE)
                        quote = c;
                    else if (quote == c)
                        quote = NOT_IN_QUOTE;
                }

                if ('>' == c && NOT_IN_QUOTE == quote) {
                    bodyStart = body + 1;
                    break;
                }
            }

            int bodyEnd = htmlDoc.indexOf(BODY_END, bodyStart);
            this.body = htmlDoc.substring(bodyStart, bodyEnd);
        }



        private static boolean isQuoteChar(char c) {
            return '"' == c || '\'' == c;
        }

        private int extractHead(String htmlDoc) {
            int headStart = htmlDoc.indexOf(HEAD_BEGIN);
            if (-1 == headStart) {
                this.head = ""; //no head tag exists
                return 0;
            }

            headStart += HEAD_BEGIN.length();

            //scan for end of <head> start tag (beginning of head section)
            char quote = NOT_IN_QUOTE;
            for (int head = headStart; head < htmlDoc.length(); head++) {
                final char c = htmlDoc.charAt(head);
                if (isQuoteChar(c)) {
                    if (quote == NOT_IN_QUOTE)
                        quote = c;
                    else if (quote == c)
                        quote = NOT_IN_QUOTE;
                }

                if ('>' == c && NOT_IN_QUOTE == quote) {

                    //check if this is a self-closing tag
                    if ('/' == htmlDoc.charAt(head - 1)) {
                        this.head = "";
                        return head + 1;
                    }

                    headStart = head + 1;
                    break;
                }
            }

            int headEnd = htmlDoc.indexOf(HEAD_END, headStart);
            this.head = htmlDoc.substring(headStart, headEnd);
            return headEnd + HEAD_END.length();
        }
    }
}
