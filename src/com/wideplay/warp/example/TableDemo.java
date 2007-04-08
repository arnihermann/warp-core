package com.wideplay.warp.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * On: 26/03/2007
 *
 * @author Dhanji R. Prasanna
 * @since 1.0
 */
public class TableDemo {
    private List<MyDVD> beans;


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


    public class MyDVD {
        private String movieName;
        private Integer price;
        private Long length;
        private Date releasedOn;


        public MyDVD(Long length, String movieName, Integer price, Date releasedOn) {
            this.length = length;
            this.movieName = movieName;
            this.price = price;
            this.releasedOn = releasedOn;
        }

        public Long getLength() {
            return length;
        }

        public void setLength(Long length) {
            this.length = length;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Date getReleasedOn() {
            return releasedOn;
        }

        public void setReleasedOn(Date releasedOn) {
            this.releasedOn = releasedOn;
        }
    }
}
