package com.wideplay.warp.widgets.rendering;

import com.wideplay.warp.widgets.Evaluator;
import net.jcip.annotations.Immutable;
import org.jetbrains.annotations.Nullable;
import org.mvel.CompileException;
import org.mvel.MVEL;
import org.mvel.ParserContext;
import org.mvel.compiler.CompiledExpression;
import org.mvel.compiler.ExpressionCompiler;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.*;

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

    //memo field caches compiled expressions
    private final Map<String, CompiledExpression> compiled = new HashMap<String, CompiledExpression>();


    public Class<?> resolveEgressType(String expression) throws ExpressionCompileException {
        return compileExpression(expression).getKnownEgressType();
    }

    public Class<?> resolveCollectionTypeParameter(String expression) throws ExpressionCompileException {

        //TODO unsafe, this assumes no nested generic type param?
        return (Class<?>) compileExpression(expression)
                .getParserContext()
                .getLastTypeParameters()[0];
    }

    public Evaluator compile(String expression) throws ExpressionCompileException {

        //do *not* inline
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
        final CompiledExpression compiledExpression = compiled.get(expression);

        //use cached copy
        if (null != compiledExpression)
            return compiledExpression;

        //otherwise compile expression and cache
        final ExpressionCompiler compiler = new ExpressionCompiler(expression, (null != backingType)
                    ? singleBackingTypeParserContext() : backingMapParserContext());

        CompiledExpression tempCompiled;
        try {
            tempCompiled = compiler.compile();

        } catch (CompileException ce) {
            throw new ExpressionCompileException(expression, ce.getErrors());
        }

        //store in memo cache
        compiled.put(expression, tempCompiled);

        return tempCompiled;
    }


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


        //read javabean properties
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            //skip getClass()
            if (CLASS.equals(propertyDescriptor.getName()))
                continue;

            //if this is a collection, determine its parametric type
            if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                final ParameterizedType returnType = (ParameterizedType) propertyDescriptor
                        .getReadMethod()
                        .getGenericReturnType();

                //box actual parametric type arguments into a Class<?> array
                List<Class<?>> typeParameters = new ArrayList<Class<?>>(1);
                typeParameters.add((Class<?>) returnType.getActualTypeArguments()[0]);

                //TODO unsafe if parametric type is a nested generic?
                context.addInput(propertyDescriptor.getName(), propertyDescriptor.getPropertyType(),
                        typeParameters.toArray(new Class[1]));

            } else
                context.addInput(propertyDescriptor.getName(), propertyDescriptor.getPropertyType());
        }

        return context;
    }
}
