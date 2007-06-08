package com.wideplay.warp.internal.components;

import com.wideplay.warp.module.ComponentRegistry;
import com.wideplay.warp.rendering.ComponentHandler;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * On: 18/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ComponentHandlerBuilderTest {
    private ComponentRegistry registry;

    @DataProvider(name = "simpleDocument")
    public Object[][] makeDocument() throws Exception {
        return new Object[][] {
                { DocumentHelper.parseText(readResourceToString("test1.xhtml")) }
        };
    }


    @BeforeClass
    public void setup() {
        registry = new DefaultComponentRegistryImpl();
    }

    @Test(dataProvider = "simpleDocument")
    public final void testBuild(Document document) {
        ComponentHandler handler = new ComponentHandlerBuilder(registry).build(document);

//        YUIFrameHtmlWriter writer = new YUIFrameHtmlWriter();
//        handler.handleRender(writer, Guice.createInjector(), as, new PageInjectDemo());

//        System.out.println(writer.toString());
    }



    private static String readResourceToString(String resource) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ComponentHandlerBuilderTest.class.getResourceAsStream(resource)));

        while(reader.ready())
            builder.append(reader.readLine());

        return builder.toString();
    }
}
