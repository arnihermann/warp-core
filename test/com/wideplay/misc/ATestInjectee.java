package com.wideplay.misc;

import com.google.inject.Inject;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail com)
 */
public class ATestInjectee {

    @Inject
    String thing;

    @Inject
    AGenericInjectee<String> stringWrap;

    @Inject
    AGenericInjectee<Number> numberWrap;

    public String toString() {
        return "ATestInjectee{" +
                "thing='" + thing + '\'' +
                ", stringWrap=" + stringWrap +
                ", numberWrap=" + numberWrap +
                '}';
    }
}
