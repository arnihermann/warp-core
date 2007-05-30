package com.wideplay.warp.example.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
* On: 30/05/2007
*
* @author Dhanji R. Prasanna
* @since 1.0
*/
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
