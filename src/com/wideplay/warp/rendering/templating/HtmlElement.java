package com.wideplay.warp.rendering.templating;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 9, 2007
 * Time: 4:02:19 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public interface HtmlElement {
    String getName();

    AttributeIterator getAttributes();

    public static interface AttributeIterator {
        boolean hasNext();
        String next();
        Object getValue();
        void setValue(Object value);
    }
}
