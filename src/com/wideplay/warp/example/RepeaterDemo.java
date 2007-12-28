package com.wideplay.warp.example;

import com.google.inject.Inject;
import com.wideplay.warp.example.model.MyDVD;

import java.util.Date;
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
    private TableDemo tableDemoPage;

    //borrow dvd list from the table demo via page-injection
    @Inject
    void setup(TableDemo tableDemoPage) {
        this.tableDemoPage = tableDemoPage;

        beans = tableDemoPage.getBeans();
    }

    public List<MyDVD> getBeans() {
        return beans;
    }

    //used in repeater to format movie release dates
    public String format(Date date) {
        return tableDemoPage.format(date);
    }
}
