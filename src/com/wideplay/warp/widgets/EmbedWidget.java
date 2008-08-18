package com.wideplay.warp.widgets;

import com.wideplay.warp.widgets.rendering.TextTools;
import com.wideplay.warp.widgets.routing.PageBook;
import net.jcip.annotations.Immutable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable
class EmbedWidget implements Renderable {
    private final Map<String, String> bindExpressions;
    private final Map<String, ArgumentWidget> arguments;
    private final Evaluator evaluator;
    private final PageBook pageBook;
    private final String targetPage;

    public EmbedWidget(Map<String, ArgumentWidget> arguments, String expression, Evaluator evaluator, PageBook pageBook, String targetPage) {
        this.arguments = arguments;

        this.evaluator = evaluator;
        this.pageBook = pageBook;
        this.targetPage = targetPage.toLowerCase();

        //parse expression list
        this.bindExpressions = TextTools.toBindMap(expression);
    }



    public void render(Object bound, Respond respond) {
        final PageBook.Page page = pageBook.forName(targetPage);

        //create an instance of the embedded page
        final Object pageObject = page.instantiate();

        //bind parameters to it as necessary
        for (Map.Entry<String, String> entry : bindExpressions.entrySet()) {
            evaluator.write(entry.getKey(), pageObject, evaluator.evaluate(entry.getValue(), bound));
        }

        //chain to embedded page (widget), with arguments
        final EmbeddedRespond embed = new EmbeddedRespond(arguments);
        page.widget().render(pageObject, embed);

        //extract and write embedded response to enclosing page's respond
        respond.writeToHead(embed.toHeadString()); //TODO only write @Require tags
        respond.write(embed.toString());
    }

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }

    static class EmbeddedRespond extends StringBuilderRespond {
        private static final String HEAD_BEGIN = "<head";
        private static final String HEAD_END = "</head>";
        private static final String BODY_BEGIN = "<body";
        private static final String BODY_END = "</body>";
        private static final char NOT_IN_QUOTE = '\0';

        //memo fields
        private String body;
        private final Map<String, ArgumentWidget> arguments;

        public EmbeddedRespond(Map<String, ArgumentWidget> arguments) {
            this.arguments = arguments;
        }


        public String toHeadString() {
            if (null == body) {
                //extract and store
                extract(super.toString());
            }

            //we discard the <head> tag rendered in the child page and
            //instead return only what was directly rendered with writeToHead()
            return getHead();
        }

        @Override
        public String toString() {
            if (null == body) {
                extract(super.toString());
            }

            return body;
        }

        public ArgumentWidget include(String name) {
            return arguments.get(name);
        }

        //state machine extracts <body> tag content 
        private void extract(String htmlDoc) {

            //now extract the contents of <body>...
            int bodyStart = htmlDoc.indexOf(BODY_BEGIN) + BODY_BEGIN.length();

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

            //if there was no body tag, just embed whatever was rendered directly
            if (-1 == bodyEnd) {
                this.body = htmlDoc;
            }
            else
                this.body = htmlDoc.substring(bodyStart, bodyEnd);
        }



        private static boolean isQuoteChar(char c) {
            return '"' == c || '\'' == c;
        }

    }
}
