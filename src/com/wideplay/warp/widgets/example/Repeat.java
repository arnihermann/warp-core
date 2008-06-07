package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 */
@At("/repeat")
public class Repeat {
    private List<String> names = Arrays.asList("Dhanji", "Josh", "Jody", "Iron Man");

    public List<String> getNames() {
        return names;
    }
}