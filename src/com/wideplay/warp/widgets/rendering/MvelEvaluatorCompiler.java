package com.wideplay.warp.widgets.rendering;

import com.wideplay.warp.util.TextTools;
import com.wideplay.warp.util.Token;
import com.wideplay.warp.widgets.Evaluator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;
import org.mvel.CompileException;
import org.mvel.MVEL;
import org.mvel.ParserContext;
import org.mvel.ast.ASTNode;
import org.mvel.compiler.CompiledExpression;
import org.mvel.compiler.ExpressionCompiler;
import org.mvel.util.ASTIterator;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
@Immutable
public class MvelEvaluatorCompiler implements EvaluatorCompiler {
    private final Class<?> backingType;
    private final Map<String, Class<?>> backingTypes;

    private static final String CLASS = "class";

    public MvelEvaluatorCompiler(Class<?> backingType) {
        this.backingType = backingType;
        this.backingTypes = null;
    }

    public MvelEvaluatorCompiler(Map<String, Class<?>> backingTypes) {
        this.backingTypes = Collections.unmodifiableMap(backingTypes);
        this.backingType = null;

    }

    public Class<?> determineEgressType(String expression) throws ExpressionCompileException {
        CompiledExpression compiledExpression = compileExpression(expression);

        ASTIterator astIterator = compiledExpression.getTokenIterator();
        while(astIterator.hasMoreNodes()) {
            ASTNode astNode = astIterator.nextNode();

            //look inside our own parsing context
        }

        return compiledExpression.getKnownEgressType();
    }

    public Evaluator compile(String expression) throws ExpressionCompileException {

        final CompiledExpression compiled = compileExpression(expression);

        return new Evaluator() {
            @Nullable
            public Object evaluate(String expr, Object bean) {
                return MVEL.executeExpression(compiled, bean);
            }

            public void write(String expr, Object bean, Object value) {
                //lets use mvel to store an expression
                MVEL.setProperty(bean, expr, value);
            }

            public Object read(String property, Object contextObject) {
                return MVEL.getProperty(property, contextObject);
            }
        };
    }

    private CompiledExpression compileExpression(String expression) throws ExpressionCompileException {
        final ExpressionCompiler compiler = new ExpressionCompiler(expression);

        CompiledExpression tempCompiled;
        try {
            tempCompiled = compiler.compile((null != backingType)
                    ? singleBackingTypeParserContext() : backingMapParserContext());

        } catch (CompileException ce) {
            throw new ExpressionCompileException(expression, ce.getErrors());
        }
        return tempCompiled;
    }

    @SuppressWarnings("unchecked")
    private ParserContext backingMapParserContext() {
        ParserContext context = new ParserContext();
        context.setStrongTyping(true);

        //noinspection unchecked
        context.addInputs((Map)backingTypes);

        return context;
    }

    public List<Token> tokenizeAndCompile(String template) throws ExpressionCompileException {
        return TextTools.tokenize(template, this);
    }

    //generates a parsing context with type information from the backing type's javabean properties
    private ParserContext singleBackingTypeParserContext() throws ExpressionCompileException {
        ParserContext context = new ParserContext();
        context.setStrongTyping(true);

        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = Introspector.getBeanInfo(backingType).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new ExpressionCompileException("Could not read class " + backingType);
        }

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //skip getClass()
            if (CLASS.equals(propertyDescriptor.getName()))
                continue;

            context.addInput(propertyDescriptor.getName(), propertyDescriptor.getPropertyType());
        }

        return context;
    }
}
