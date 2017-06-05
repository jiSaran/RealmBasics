package com.saran.test.realmbasics.database;

import io.realm.RealmObject;

public class PhoneModel extends RealmObject {
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
