package com.wideplay.warp.widgets;

import java.util.Properties;
import java.util.Map;
import java.io.IOException;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class StringBufferRespond implements Respond {
    private final StringBuilder out = new StringBuilder();

    public void write(String text) {
        out.append(text);
    }

    public HtmlTagBuilder withHtml() {
        return new HtmlBuilder();
    }

    public void write(char c) {
        out.append(c);
    }

    public void chew() {
        out.deleteCharAt(out.length() - 1);
    }

    public String toString() {
        return out.toString();
    }

    //do NOT make this a static inner class!
    private class HtmlBuilder implements HtmlTagBuilder {
        private static final String TEXT_TAG_TEMPLATE = "warp-servlet.template.textfield";
        private final Map<String, String> templates;

        private HtmlBuilder() {
            final Properties properties = new Properties();
            try {
                properties.load(StringBufferRespond.class.getResourceAsStream("templates.properties"));
            } catch (IOException e) {
                throw new NoSuchResourceException("Can't find templates.properties", e);
            }

            //noinspection unchecked
            templates = (Map)properties;
        }

        public void textField(String bind, String value) {
            write(String.format(templates.get(TEXT_TAG_TEMPLATE), bind, value));
        }
    }
}
