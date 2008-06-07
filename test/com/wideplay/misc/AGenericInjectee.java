package com.wideplay.misc;

import com.google.inject.Inject;

import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
*/
public class AGenericInjectee<T> {
    @Inject
    private List<T> list;

    @Inject
    AnotherGenericInjectee<T> anotherGenericInjectee;

    public List<T> getList() {
        return list;
    }

    public String toString() {
        return "AGenericInjectee{" +
                "list=" + list +
                ", anotherGenericInjectee=" + anotherGenericInjectee +
                '}';
    }
}
