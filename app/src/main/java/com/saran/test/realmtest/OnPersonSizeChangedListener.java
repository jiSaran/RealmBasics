package com.saran.test.realmtest;

import com.saran.test.realmtest.database.Person;

import io.realm.RealmResults;

/**
 * Created by core I5 on 6/2/2017.
 */

public interface OnPersonSizeChangedListener {
    void onDataChangedListener(RealmResults<Person> personRealmResults);
}
