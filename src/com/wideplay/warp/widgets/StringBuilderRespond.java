package com.wideplay.warp.widgets;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
class StringBuilderRespond implements Respond {

    private static final String TEXT_TAG_TEMPLATE = "warp-servlet.template.textfield";
    private static final String HEADER_PLACEHOLDER = "__w:w:HEADER_PLACEhOlDeR:NOWRITe::__";

    private static volatile Map<String, String> templates;

    StringBuilderRespond() {
        //unsynchronized overwrite (contending overwrites are fine)
        if (null == templates) {
            final Properties properties = new Properties();
            try {
                properties.load(StringBuilderRespond.class.getResourceAsStream("templates.properties"));
            } catch (IOException e) {
                throw new NoSuchResourceException("Can't find templates.properties", e);
            }

            //noinspection unchecked
            templates = (Map)properties;
        }
    }

    private final StringBuilder out = new StringBuilder();
    private final StringBuilder head = new StringBuilder();

    public void write(String text) {
        out.append(text);
    }

    public HtmlTagBuilder withHtml() {
        return new HtmlBuilder();
    }

    public void write(char c) {
        out.append(c);
    }

    public void writeToHead(String text) {
        head.append(text);
    }

    public void chew() {
        out.deleteCharAt(out.length() - 1);
    }

    @Override
    public String toString() {
        int index = out.indexOf(HEADER_PLACEHOLDER);        //TODO optimize by scanning upto <body> only

        String output = out.toString();

        if (index > 0) {
            output = output.replaceFirst(HEADER_PLACEHOLDER, head.toString());
        }

        return output;
    }

    //do NOT make this a static inner class!
    private class HtmlBuilder implements HtmlTagBuilder {

        public void textField(String bind, String value) {
            write(String.format(templates.get(TEXT_TAG_TEMPLATE), bind, value));
        }

        public void headerPlaceholder() {
            write(HEADER_PLACEHOLDER);
        }
    }
}
