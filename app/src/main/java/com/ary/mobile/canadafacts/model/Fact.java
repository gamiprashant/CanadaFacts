package com.ary.mobile.canadafacts.model;

/**
 * Created by gamiprashant on 10/02/2015.
 *
 * Model Class for Fact
 */
public class Fact {
    public final String title;
    public final String description;
    public final String imageUrl;


    //////////////////////////////////////////////////////////////////
    public Fact(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
