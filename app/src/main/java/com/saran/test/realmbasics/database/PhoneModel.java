package com.saran.test.realmbasics.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PhoneModel extends RealmObject {
    private String type;
    private String number;

    @PrimaryKey
    private int id;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
