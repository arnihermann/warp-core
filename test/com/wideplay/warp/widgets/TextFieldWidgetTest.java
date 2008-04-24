package com.wideplay.warp.widgets;

import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public class TextFieldWidgetTest {

    @Test
    public final void textTagRender() {

        final String[] out = new String[1];
        Respond mockRespond = new StringBuilderRespond() {
            @Override
            public void write(String text) {
                out[0] = text;
            }
        };
        final String boundTo = "aString";

        new TextFieldWidget(new WidgetChain(), "boundTo", new MvelEvaluator())
                .render(new HashMap<String, Object>() {{
                    put("boundTo", boundTo);
                }}, mockRespond);


        //assert the validity of the text tag:
        assert out[0] != null : "Nothing rendered!";
        String tag = out[0].trim();

        assert tag.startsWith("<input ");
        assert tag.endsWith(">");
        assert tag.contains("value=\"" + boundTo + "\"");
        assert tag.contains("name=\"boundTo\"");
        assert tag.contains("type=\"text\"");
    }
}
