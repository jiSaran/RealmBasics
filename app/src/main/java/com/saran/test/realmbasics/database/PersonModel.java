package com.saran.test.realmbasics.database;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

//Should extend RealmObject to be added in database. This class acts as a table in sql.
public class PersonModel extends RealmObject {
    private String name;
    private int age;
    private RealmList<PetModel> pets;
    private RealmList<PhoneModel> phones;

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

    public void setPets(RealmList<PetModel> pets) {
        this.pets = pets;
    }

    public List<PetModel> getPets() {
        return pets;
    }

    public void setPhones(RealmList<PhoneModel> phones) {
        this.phones = phones;
    }

    public List<PhoneModel> getPhones() {
        return phones;
    }
}
