package com.radioline.master.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by mikoladyachok on 1/15/15.
 */
@ParseClassName("Actions")
public class Actions extends ParseObject {
    public Actions() {

    }


    public static ParseQuery<Actions> getQuery() {
        return ParseQuery.getQuery(Actions.class);
    }


}
