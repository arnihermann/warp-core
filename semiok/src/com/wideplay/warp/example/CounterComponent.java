package com.wideplay.warp.example;

import com.wideplay.warp.annotations.Component;
import com.wideplay.warp.annotations.Template;

/**
 * Created with IntelliJ IDEA.
 * On: 20/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@Component(name = "counter")
@Template(name = "CounterComponentTemplate.html")
public class CounterComponent {
    private int counter;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
