package com.wideplay.warp.widgets;

import com.wideplay.warp.util.Token;
import static com.wideplay.warp.util.TextTools.*;
import com.wideplay.warp.widgets.rendering.SelfRendering;

import java.util.List;

import net.jcip.annotations.ThreadSafe;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @SelfRendering
class TextWidget implements RenderableWidget {
    private final String template;
    private volatile List<Token> tokenizedTemplate;     //TODO store some metrics to allocate buffers later
    private final Evaluator evaluator;

    TextWidget(String template, Evaluator evaluator) {
        this.template = template;
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        //warm up cache
        if (null == tokenizedTemplate)
            tokenizedTemplate = tokenize(template);

        //rebuild template from tokens
        StringBuilder builder = new StringBuilder();
        for (Token token : tokenizedTemplate) {
            if (token.isExpression())
                builder.append(evaluator.evaluate(token.getToken(), bound));
            else
                builder.append(token.getToken());
        }

        respond.write(builder.toString());
    }
}
