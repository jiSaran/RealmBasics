package com.saran.test.realmbasics;

import android.app.Application;

import com.saran.test.realmbasics.database.RealmMigrate;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/** Class where realm configurations are set and initialized**/

public class RealmBasicsApplication extends Application {
    private static Realm realm;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("mydb.realm")
                .schemaVersion(2)
                .migration(new RealmMigrate()) //Use RealmMigrate class to migrate data on schema version change
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();
    }

    public static Realm getRealmInstance(){
        if(realm!=null){
            return realm;
        } else {
            return Realm.getDefaultInstance();
        }
    }
}
