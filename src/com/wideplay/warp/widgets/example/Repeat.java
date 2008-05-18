package com.wideplay.warp.widgets.example;

import com.wideplay.warp.widgets.At;
import com.wideplay.warp.widgets.Show;

import java.util.List;
import java.util.Arrays;

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