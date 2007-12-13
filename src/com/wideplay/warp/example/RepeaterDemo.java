package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.wideplay.warp.example.model.MyDVD;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 26/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
public class RepeaterDemo {
    private List<MyDVD> beans;

    //borrow dvd list from the table demo via page-injection
    @Inject
    private void setup(TableDemo tableDemoPage) {
        beans = tableDemoPage.getBeans();
    }

    public List<MyDVD> getBeans() {
        return beans;
    }
}
