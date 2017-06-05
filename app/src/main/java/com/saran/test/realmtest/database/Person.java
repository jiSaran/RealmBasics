package com.saran.test.realmtest.database;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by core I5 on 2/21/2017.
 */

public class Person extends RealmObject {
    private String name;
    private int age;
    private RealmList<Pet> pets;
    private RealmList<Phone> phones;

    @PrimaryKey
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }

    public void setPets(RealmList<Pet> pets) {
        this.pets = pets;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPhones(RealmList<Phone> phones) {
        this.phones = phones;
    }

    public List<Phone> getPhones() {
        return phones;
    }
}
