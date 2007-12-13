package com.wideplay.warp.rendering;

import com.wideplay.warp.rendering.templating.HtmlElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: dhanji
 * Date: Dec 13, 2007
 * Time: 5:10:07 PM
 *
 * @author Dhanji R. Prasanna (dhanji gmail com)
 */
public class UnfilteredHtmlElementTest {
    private static final String TAG_DATA = "tagData";

    @DataProvider(name = TAG_DATA)
    Object[][] getTagData() {
        return new Object[][] {
                {"script", new Object[] { } },
                {"script", new Object[] { "id", "one", "blah", 246L, "blah", 246L, "blah", 246L, } },
                {"script", new Object[] { "id", "one", "blah", 246L, "blah", 246L, "blah", 246L,"blah", 246L,"blah", 246L,"blah", 246L } },
                {"script", new Object[] { "id", "one", "blah", 246L } },
        };
    }

    @Test(dataProvider = TAG_DATA)
    public final void attributeIteration(String name, final Object[] nameValuePairs) {
        //create mocks


        //begin mock script ***
        final HtmlElement.AttributeIterator attributeIterator = new UnfilteredHtmlElement(name, nameValuePairs)
                .getAttributes();

        //run mock script ***
        while(attributeIterator.hasNext()) {
            String attr = attributeIterator.next();

            System.out.println(attributeIterator.getValue());
            attributeIterator.setValue("blah");
        }


        //assert expectations
    }
}
