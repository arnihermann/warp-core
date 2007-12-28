package com.wideplay.warp.example;

import com.wideplay.warp.example.model.MyDVD;
import com.wideplay.warp.annotations.Template;
import com.wideplay.warp.annotations.URIMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * On: 26/03/2007
 *
 * @author Dhanji R. Prasanna (dhanji at gmail com)
 * @since 1.0
 */
@URIMapping("/TableDemo")
@Template(name = "TableDemo.html")
public class TableDemo {
    private List<MyDVD> beans;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yy");


    public TableDemo() {
        beans = new ArrayList<MyDVD>();
        beans.add(new MyDVD(110L, "Under Siege", 20, new Date()));
        beans.add(new MyDVD(240L, "A Clockwork Orange", 50, new Date()));
        beans.add(new MyDVD(220L, "Gears of War", 20, new Date()));
        beans.add(new MyDVD(210L, "Family Guy", 30, new Date()));
        beans.add(new MyDVD(205L, "Eraserhead", 50, new Date()));
    }

    public List<MyDVD> getBeans() {
        return beans;
    }


    //used in table to format movie release dates
    public String format(Date date) {
        return simpleDateFormat.format(date);
    }

}
