package com.saran.test.realmbasics.database;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/** This class migrates the data from one verison to another while retaining its data**/

public class RealmMigrate implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if(oldVersion == 1){
            //A new field origin was added in the PetModel realmObject. So add field origin in exisiting PetModel objects
            final RealmObjectSchema petSchema = schema.get("PetModel");
            petSchema.addField("origin",String.class);
        }
    }
}
