package com.saran.test.realmtest;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by core I5 on 5/30/2017.
 */

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if(oldVersion == 1){
            final RealmObjectSchema petSchema = schema.get("Pet");
            petSchema.addField("origin",String.class);
        }
    }
}
