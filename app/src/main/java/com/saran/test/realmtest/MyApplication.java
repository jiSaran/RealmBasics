package com.saran.test.realmtest;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by core I5 on 6/2/2017.
 */

public class MyApplication extends Application {
    private static Realm realm;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("mydb.realm")
                .schemaVersion(2)
                .migration(new MyMigration())
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
