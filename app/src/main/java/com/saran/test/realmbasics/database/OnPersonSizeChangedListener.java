package com.saran.test.realmbasics.database;

import com.saran.test.realmbasics.database.PersonModel;

import io.realm.RealmResults;

/**Interface that provides callback on datachange in PersonModel**/
public interface OnPersonSizeChangedListener {
    void onDataChangedListener(RealmResults<PersonModel> personRealmResults);
}
