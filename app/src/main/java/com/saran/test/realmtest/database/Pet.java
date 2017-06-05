package com.saran.test.realmtest.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by core I5 on 5/23/2017.
 */

public class Pet extends RealmObject {
    private String name;
    private String type;
    private String origin;

    @PrimaryKey
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
