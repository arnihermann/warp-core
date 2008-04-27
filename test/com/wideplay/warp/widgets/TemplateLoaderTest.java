package com.wideplay.warp.widgets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class TemplateLoaderTest {
    private static final String CLASSES_AND_TEMPLATES = "classesAndTemplates";

    @DataProvider(name = CLASSES_AND_TEMPLATES)
    public Object[][] get() {
        return new Object[][] {
            { MyXmlPage.class },
            { My.class },
            { MyXhtml.class },
            { MyHtml.class },
        };
    }

    @Test(dataProvider = CLASSES_AND_TEMPLATES)
    public final void loadExplicitXmlTemplate(final Class<MyXmlPage> pageClass) {
        String template = new TemplateLoader()
                    .load(pageClass);

        assert null != template : "no template found!";
        assert template.startsWith("<xml>") && template.endsWith("</xml>"); //a weak sauce test
    }

    @Show("My.xml")
    public static class MyXmlPage { }


    public static class My { }
    public static class MyXhtml { }
    public static class MyHtml { }
}
