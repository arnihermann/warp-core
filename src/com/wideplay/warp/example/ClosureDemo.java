package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.wideplay.warp.annotations.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 26/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class ClosureDemo {
    private List<TableDemo.MyDVD> beans;

    //borrow dvd list from the table demo via page-injection
    @Inject
    private void setup(@Page TableDemo tableDemoPage) {
        beans = tableDemoPage.getBeans();
    }

    public List<TableDemo.MyDVD> getBeans() {
        return beans;
    }
}
