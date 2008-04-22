package com.wideplay.warp.widgets;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class TextWidgetTest {
    private static final String NAME_VALUES = "nameValues";
    private static final String MVEL_NAMES = "mvelNames";

    @DataProvider(name = NAME_VALUES)
    Object[][] getNameValues() {
        return new Object[][] {
                { "Dhanji" },
                { "Joe" },
                { "Josh" },
        };
    }

    @DataProvider(name = MVEL_NAMES)    //creates a path for expr: ${names.first}
    Object[][] getMvelNames() {
        return new Object[][] {
                { new HashMap<String,Object>() {{
                    put("names", new HashMap<String, String>() {{
                        put("first", "Dhanji");
                        put("second", "NotDhanji");
                    }});

                    }}, "Dhanji" },

                { new HashMap<String,Object>() {{
                    put("names", new HashMap<String, String>() {{
                        put("first", "Joei");
                        put("second", "NotDhanji");
                    }});

                    }}, "Joei" },

                { new HashMap<String,Object>() {{
                    put("names", new HashMap<String, String>() {{
                        put("first", "Jill");
                        put("second", "NotDhanji");
                    }});

                    }}, "Jill" },


        };
    }

    @Test(dataProvider = NAME_VALUES)
    public final void renderATemplateWithObject(final String name) {
        final String[] out = new String[1];
        Respond respond = new StringBufferRespond() {
            public void write(String string) {
                out[0] = string;
            }
        };

        new TextWidget("Hello ${name}", new MvelEvaluator())
                .render(new HashMap<String, String>(){{
                    put("name", name);
                }}, respond);

        assert ("Hello " + name).equals(out[0]) : "template render failed: " + out[0];
    }

    @Test(dataProvider = MVEL_NAMES)
    public final void renderATemplateWithObjectGraph(final Map<String, String> data, String name) {
        final String[] out = new String[1];
        Respond respond = new StringBufferRespond() {
            public void write(String string) {
                out[0] = string;
            }
        };

        new TextWidget("Hello ${names.first}", new MvelEvaluator())
                .render(data, respond);

        assert ("Hello " + name).equals(out[0]) : "template render failed: " + out[0];
    }
}
