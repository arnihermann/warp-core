package com.wideplay.warp.widgets;

import net.jcip.annotations.NotThreadSafe;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@NotThreadSafe
class SaxLineNumbersFilter extends XMLFilterImpl {
    public static final String LINE_NUMBER_ATTRIBUTE = "__WarpWidgetsSaxLineNumber";

    private Locator locator;

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;

        super.setDocumentLocator(locator);
    }

    public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {

        //replace existing attributes with a decorator that stores line numbers
        AttributesImpl attr = new AttributesImpl(attributes);
        attr.addAttribute("", "", LINE_NUMBER_ATTRIBUTE, "int", String.valueOf(locator.getLineNumber()));

        super.startElement(s, s1, s2, attr);
    }

}
