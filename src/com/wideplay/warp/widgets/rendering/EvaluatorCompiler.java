package com.wideplay.warp.widgets.rendering;

import com.wideplay.warp.util.Token;
import com.wideplay.warp.widgets.Evaluator;
import org.mvel.ErrorDetail;

import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface EvaluatorCompiler {
    Evaluator compile(String expression) throws ExpressionCompileException;

    Class<?> determineEgressType(String expression) throws ExpressionCompileException;

    List<Token> tokenizeAndCompile(String template) throws ExpressionCompileException;

    public static class CompileErrorDetail {
        private final String expression;
        private final ErrorDetail error;

        public CompileErrorDetail(String expression, ErrorDetail error) {
            this.expression = expression;
            this.error = error;
        }


        public String getExpression() {
            return expression;
        }

        public ErrorDetail getError() {
            return error;
        }
    }
}
