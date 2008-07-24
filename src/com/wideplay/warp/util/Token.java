package com.wideplay.warp.util;

import com.wideplay.warp.widgets.Evaluator;
import com.wideplay.warp.widgets.rendering.EvaluatorCompiler;
import com.wideplay.warp.widgets.rendering.ExpressionCompileException;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * A simple wrapper around a string denoting it as an token.
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class Token {
    private final String token;
    private final boolean isExpression;
    private final Evaluator evaluator;

    private Token(String token, boolean expression) {
        this.token = token;
        this.evaluator = null;
        isExpression = expression;
    }

    private Token(Evaluator evaluator, boolean expression) {
        this.evaluator = evaluator;
        isExpression = expression;
        this.token = null;
    }

    public boolean isExpression() {
        return isExpression;
    }

    public Object render(Object bound) {
        return isExpression ? evaluator.evaluate(null, bound) : token;
    }

    static Token expression(String token, EvaluatorCompiler compiler) throws ExpressionCompileException {
        //strip leading ${ and trailing }
        return new Token(compiler.compile(token.substring(2, token.length() - 1)), true);
    }

    static Token text(String token) {
        return new Token(token, false);
    }
}
