package com.wideplay.misc;

import com.google.inject.Inject;

import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class AnotherGenericInjectee<T> {
    @Inject
    private List<T> ts;

    public List<T> getTs() {
        return ts;
    }

    public String toString() {
        return "AnotherGenericInjectee{" +
                "ts=" + ts +
                '}';
    }
}
