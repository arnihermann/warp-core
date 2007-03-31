package com.wideplay.warp.internal;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import ognl.Ognl;
import ognl.OgnlException;

import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * On: 27/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class OgnlTest {
    private List<String> strings = new ArrayList<String>();
    private String myString;

    @BeforeClass
    public void startup() {
        strings.add("yahop");
        strings.add("goog");
        strings.add("asdasdp");
    }

    @Test
    public final void testSetValue() throws OgnlException {
        Object value = "goog".hashCode();

        Ognl.setValue("myString", this, value);
        System.out.println(myString);

        System.out.println(Ognl.getValue("myString = strings.{? #this.hashCode() == " + value + "}[0]", this));
        System.out.println(myString);
    }


    
    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }
}
