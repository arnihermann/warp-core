package com.wideplay.warp.widgets;

import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.Token;
import com.wideplay.warp.widgets.rendering.SelfRendering;
import net.jcip.annotations.Immutable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@Immutable @SelfRendering
class RequireWidget implements Renderable {
    private final List<Token> template;
    private final Evaluator evaluator;

    public RequireWidget(String xml, Evaluator evaluator) {
        this.template = TextTools.tokenize(xml);
        this.evaluator = evaluator;
    }

    public void render(Object bound, Respond respond) {
        //rebuild template from tokens
        StringBuilder builder = new StringBuilder();
        for (Token token : template) {
            if (token.isExpression())
                builder.append(evaluator.evaluate(token.getToken(), bound));
            else
                builder.append(token.getToken());
        }

        //special method interns tokens
        respond.require(builder.toString());
    }

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }
}
