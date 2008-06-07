package com.wideplay.warp.widgets;

import static com.wideplay.warp.util.TextTools.tokenize;
import com.wideplay.warp.util.Token;
import com.wideplay.warp.widgets.rendering.SelfRendering;
import net.jcip.annotations.ThreadSafe;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@ThreadSafe @SelfRendering
class TextWidget implements Renderable {
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


    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }
}
