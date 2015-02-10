package com.ary.mobile.canadafacts.util;

/**
 * Created by gamiprashant on 11/02/2015.
 */
public class AryCommons {

    //////////////////////////////////////////////////////////////////
    public static String safe(String str) {
        //sanitizing string for any data issues
        if(str == null || str.isEmpty() || str.equalsIgnoreCase("null"))
            return "";
        else return str;
    }
}
