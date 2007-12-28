package com.wideplay.warp.module.componentry;

import net.jcip.annotations.Immutable;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Immutable
public final class PropertyDescriptor {
    private final String name;
    private final String value;
    private final boolean isExpression;


    public PropertyDescriptor(String name, String value, boolean expression) {
        this.name = name;
        this.value = value;
        isExpression = expression;
    }

    public boolean isExpression() {
        return isExpression;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
