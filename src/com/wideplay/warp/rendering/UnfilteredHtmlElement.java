package com.wideplay.warp.rendering;

import com.wideplay.warp.rendering.templating.HtmlElement;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 9, 2007
 * Time: 3:39:29 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
//@NotThreadSafe
class UnfilteredHtmlElement implements HtmlElement {
    private final String name;
    private final AttributeIterator attributes;
    private final Object[] nameValuePairs;


    public UnfilteredHtmlElement(String name, final Object[] nameValuePairs) {
        this.name = name;
        this.nameValuePairs = nameValuePairs;
        this.attributes = new AttributeIterator() {
            private int current = -1;

            public Object getValue() {
                return nameValuePairs[current];
            }

            public void setValue(Object value) {
                nameValuePairs[current] = value;
            }

            public String next() {
                String attr = (String) nameValuePairs[current + 1];
                current+=2;
                
                return attr;
            }

            public boolean hasNext() {
                return nameValuePairs.length > 0 && current < nameValuePairs.length-1; 
            }
        };
    }

    public String getName() {
        return name;
    }

    public AttributeIterator getAttributes() {
        return attributes;
    }

    public Object[] getNameValuePairs() {
        return nameValuePairs;
    }
}
