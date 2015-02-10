package com.ary.mobile.canadafacts.model;

import java.util.List;

/**
 * Created by gamiprashant on 10/02/2015.
 *
 * Response Object for JSON
 */
public class FactResponse {
    public final String title;
    public final List<Fact> facts;

    public FactResponse(String title, List<Fact> facts) {
        this.title = title;
        this.facts = facts;
    }
}
