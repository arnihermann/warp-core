package com.wideplay.warp.widgets.rendering;

import com.wideplay.warp.widgets.Evaluator;
import org.mvel.ErrorDetail;

import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
public interface EvaluatorCompiler {

    /**
     *
     * @param expression An expression to compile against this compiler (and context class)
     * @return Returns An evaluator that can execute the expression against the context class
     *  and a real instance of data.
     *
     * @throws ExpressionCompileException If there is a problem compiling the expression.
     */
    Evaluator compile(String expression) throws ExpressionCompileException;


    /**
     *
     * @param expression An expression to compile against this compiler (and context class)
     * @return Returns the type parameter OF the egress type of the entire expression chain, *assuming*
     *  that it is a collection (or really, more like a single parametric type).
     *
     * @throws ExpressionCompileException If there is a problem compiling the expression.
     * @see #resolveEgressType(String)
     */
    Class<?> resolveCollectionTypeParameter(String expression) throws ExpressionCompileException;


    /**
     *
     * @param template A String template with embedded EL expressions to convert into a list of evaluable
     *  tokens. These tokens evaluate against given data or simply render plain text as appropriate. Performs
     *  a compile of each expression against the context class of this EvaluatorCompiler.
     *
     * @return Returns the egress type of the entire expression chain
     *
     * @throws ExpressionCompileException If there is a problem compiling the expression.
     */
    List<Token> tokenizeAndCompile(String template) throws ExpressionCompileException;

    /**
     *
     * @param expression An expression to compile against this compiler (and context class)
     * @return Returns the egress type of the entire expression chain
     *
     * @throws ExpressionCompileException If there is a problem compiling the expression.
     */
    Class<?> resolveEgressType(String expression) throws ExpressionCompileException;

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
