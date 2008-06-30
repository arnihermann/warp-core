package com.wideplay.warp.widgets.rendering;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public interface Compiled<T> {
    Object evaluate(T instance);
}
