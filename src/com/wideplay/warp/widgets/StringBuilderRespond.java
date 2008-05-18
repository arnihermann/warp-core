package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@NotThreadSafe
class StringBuilderRespond implements Respond {

    private static final String TEXT_TAG_TEMPLATE = "warp-servlet.template.textfield";
    private static final String HEADER_PLACEHOLDER = "__w:w:HEADER_PLACEhOlDeR:NOWRITe::__";

    private static final AtomicReference<Map<String, String>> templates = new AtomicReference<Map<String, String>>();
    private static final String TEXT_HTML = "text/html";

    StringBuilderRespond() {
        if (null == templates.get()) {
            final Properties properties = new Properties();
            try {
                properties.load(StringBuilderRespond.class.getResourceAsStream("templates.properties"));
            } catch (IOException e) {
                throw new NoSuchResourceException("Can't find templates.properties", e);
            }

            //noinspection unchecked
            templates.compareAndSet(null, (Map)properties);
        }
    }

    private final StringBuilder out = new StringBuilder();
    private final StringBuilder head = new StringBuilder();
    private final Set<String> requires = new HashSet<String>();
    private String redirect;

    protected String getHead() {
        return head.toString();
    }

    public void write(String text) {
        out.append(text);
    }

    public HtmlTagBuilder withHtml() {
        return new HtmlBuilder();
    }

    public void write(char c) {
        out.append(c);
    }

    public void require(String require) {
        requires.add(require);
    }

    public void redirect(String to) {
        this.redirect = to;
    }

    public void writeToHead(String text) {
        head.append(text);
    }

    public void chew() {
        out.deleteCharAt(out.length() - 1);
    }

    public String getRedirect() {
        return redirect;
    }

    public String getContentType() {
        return TEXT_HTML;
    }

    @Override
    public String toString() {
        //write requires to header first...
        for (String require : requires) {
            writeToHead(require);
        }

        //write header to placeholder...
        int index = out.indexOf(HEADER_PLACEHOLDER);        //TODO optimize by scanning upto <body> only (if no head)

        String output = out.toString();

        if (index > 0) {
            output = output.replaceFirst(HEADER_PLACEHOLDER, head.toString());
        }

        return output;
    }

    //do NOT make this a static inner class!  TODO: absorb into enclosing class (Saves on memory allocation)
    private class HtmlBuilder implements HtmlTagBuilder {

        public void textField(String bind, String value) {
            write(String.format(templates.get().get(TEXT_TAG_TEMPLATE), bind, value));
        }

        public void headerPlaceholder() {
            write(HEADER_PLACEHOLDER);
        }
    }
}
