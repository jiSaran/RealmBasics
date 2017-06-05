package com.saran.test.realmtest.database;

import io.realm.RealmObject;

/**
 * Created by core I5 on 5/23/2017.
 */

public class Phone extends RealmObject {
    public String type;
    public String number;

    public void setType(String type) {
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }
}
