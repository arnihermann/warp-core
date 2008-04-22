package com.wideplay.warp.widgets;

import com.wideplay.warp.util.Token;
import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.widgets.rendering.Attributes;
import com.wideplay.warp.widgets.rendering.SelfRendering;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

import net.jcip.annotations.ThreadSafe;

/**
 * <p>
 * Widget renders an XML-like tag
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @SelfRendering
class XmlWidget implements RenderableWidget {
    private final WidgetChain chain;
    private final boolean noChildren;
    private final String name;
    private final Evaluator evaluator;
    private final Map<String, List<Token>> attributes;


    XmlWidget(WidgetChain chain, String name, Evaluator evaluator, @Attributes Map<String, String> attributes) {
        this.chain = chain;
        this.name = name;
        this.evaluator = evaluator;
        this.attributes = Collections.unmodifiableMap(tokenize(attributes));
        this.noChildren = chain instanceof TerminalWidgetChain;
    }

    //converts a map of name:value attrs into a map of name:token attrs
    private Map<String, List<Token>> tokenize(Map<String, String> attributes) {
        Map<String, List<Token>> map = new LinkedHashMap<String, List<Token>>();

        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            map.put(attribute.getKey(), TextTools.tokenize(attribute.getValue()));
        }

        return map;
    }

    public void render(Object bound, Respond respond) {
        respond.write('<');
        respond.write(name);

        respond.write(' ');

        //write attributes
        for (Map.Entry<String, List<Token>> attribute : attributes.entrySet()) {
            respond.write(attribute.getKey());
            respond.write("=\"");

            for (Token token : attribute.getValue()) {
                if (token.isExpression()) {
                    final Object value = evaluator.evaluate(token.getToken(), bound);

                    //normalize nulls to "null" (i.e. let responder take care of writing it)
                    respond.write((null == value) ? (String)value : value.toString());
                }
                else
                    respond.write(token.getToken());
            }

            respond.write("\" ");
        }

        respond.chew();

        //write children
        if (noChildren) {
            respond.write("/>");    //write self-closed tag
        } else {
            respond.write('>');
            chain.render(bound, respond);

            //close tag
            respond.write("</");
            respond.write(name);
            respond.write('>');
        }

    }
}