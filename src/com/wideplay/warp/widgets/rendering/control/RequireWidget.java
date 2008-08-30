package com.wideplay.warp.widgets.rendering.control;

import com.wideplay.warp.widgets.Renderable;
import com.wideplay.warp.widgets.Respond;
import com.wideplay.warp.widgets.compiler.EvaluatorCompiler;
import com.wideplay.warp.widgets.compiler.ExpressionCompileException;
import com.wideplay.warp.widgets.compiler.Token;
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

    public RequireWidget(String xml, EvaluatorCompiler compiler) throws ExpressionCompileException {
        this.template = compiler.tokenizeAndCompile(xml);
    }

    public void render(Object bound, Respond respond) {
        //rebuild template from tokens
        StringBuilder builder = new StringBuilder();
        for (Token token : template) {
                builder.append(token.render(bound));
        }

        //special method interns tokens
        respond.require(builder.toString());
    }

    public <T extends Renderable> Set<T> collect(Class<T> clazz) {
        return Collections.emptySet();
    }
}
