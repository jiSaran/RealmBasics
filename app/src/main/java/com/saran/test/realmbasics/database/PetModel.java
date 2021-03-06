package com.saran.test.realmbasics.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PetModel extends RealmObject {
    private String name;
    private String type;
    private String origin;

    @PrimaryKey
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getOrigin() {
        return origin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
