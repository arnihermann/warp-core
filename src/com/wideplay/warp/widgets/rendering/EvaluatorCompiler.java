package com.wideplay.warp.widgets.rendering;

import com.wideplay.warp.widgets.Evaluator;
import org.mvel.MVEL;
import org.mvel.ParserContext;
import org.mvel.CompileException;
import org.mvel.compiler.ExpressionCompiler;
import org.mvel.compiler.CompiledExpression;
import org.jetbrains.annotations.Nullable;

import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class EvaluatorCompiler<T> {
    private final Class<T> backingType;
    private static final String CLASS = "class";

    public EvaluatorCompiler(Class<T> backingType) {
        this.backingType = backingType;
    }

    public Evaluator compile(String expression) throws ExpressionCompileException {
        final ExpressionCompiler compiler = new ExpressionCompiler(expression);

        CompiledExpression tempCompiled;
        try {
            tempCompiled = compiler.compile(backingTypeParserContext());
        } catch (CompileException ce) {
            throw new ExpressionCompileException("Could not compile expression because " + ce.getMessage());
        }

        final CompiledExpression compiled = tempCompiled;

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

    //generates a parsing context with type information from the backing type's javabean properties
    private ParserContext backingTypeParserContext() throws ExpressionCompileException {
        ParserContext context = new ParserContext();
        context.setStrongTyping(true);

        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = Introspector.getBeanInfo(backingType).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new ExpressionCompileException("Could not compile expression for class " + backingType);
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
