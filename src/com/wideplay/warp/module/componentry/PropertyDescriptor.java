package com.wideplay.warp.module.componentry;

/**
 * Created with IntelliJ IDEA.
 * On: 17/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public final class PropertyDescriptor {
    private String name;
    private String value;
    private boolean isExpression;


    public PropertyDescriptor(String name, String value, boolean expression) {
        this.name = name;
        this.value = value;
        isExpression = expression;
    }

    public boolean isExpression() {
        return isExpression;
    }

    public void setExpression(boolean expression) {
        isExpression = expression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
