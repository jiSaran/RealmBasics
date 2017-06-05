package com.saran.test.realmbasics.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.saran.test.realmbasics.RealmBasicsApplication;
import com.saran.test.realmbasics.OnPersonSizeChangedListener;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/** Helper class for various operations on Database**/

public class DBHelper {
    private Context mContext;
    private Realm mRealm;
    private SharedPreferences pref;
    private RealmChangeListener<RealmResults<PersonModel>> changeListener;
    private RealmResults<PersonModel> personRealmResults;

    public DBHelper(Context context, final OnPersonSizeChangedListener listener){
        mContext = context;
        mRealm = RealmBasicsApplication.getRealmInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        //Register new listener for reactive approach in PersonModel(realm object)
        changeListener = new RealmChangeListener<RealmResults<PersonModel>>() {
            @Override
            public void onChange(RealmResults<PersonModel> o) {
                listener.onDataChangedListener(o);
            }
        };
        personRealmResults = mRealm.where(PersonModel.class).findAllSortedAsync("name"); //Query all data of PersonModel
        personRealmResults.addChangeListener(changeListener);
    }

    public void addPerson(@NonNull final String name, @NonNull final int age, @NonNull final List<PetModel> pets, @NonNull final List<PhoneModel> phones){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (pref != null) {
                    int id = pref.getInt("id", 0) + 1;
                    PersonModel person = realm.createObject(PersonModel.class, id); //Persisted on db
                    person.setName(name+id);
                    person.setAge(age);
                    for (int i=0; i<pets.size(); i++){
                        PetModel pet = realm.createObject(PetModel.class, UUID.randomUUID().toString()); //UUID generates unique string
                        pet.setName(pets.get(i).getName());
                        pet.setType(pets.get(i).getType());
                        pet.setOrigin(pets.get(i).getOrigin());
                        person.getPets().add(pet); // get List and add pet
                    }
                    for (int i=0; i<phones.size(); i++){
                        PhoneModel phone = realm.createObject(PhoneModel.class);
                        phone.setNumber(phones.get(i).getNumber());
                        phone.setType(phones.get(i).getType());
                        person.getPhones().add(phone);
                    }
                    pref.edit().putInt("id",id).commit();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "PersonModel added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void addPets(final RealmList<PetModel> petList){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(petList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "PetModel added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void addPhone(final PhoneModel phone){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(phone); //persisted on db
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "PhoneModel added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }


    public void updatePerson(final int id, final String name){
        //Update person name according to id
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PersonModel person = realm.where(PersonModel.class).equalTo("id",id).findFirst(); //query should be inside transaction because update should be done in same thread
                person.setName(name);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "PersonModel updated successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void deleteMobilePhones(){
        //Delete all the mobile phone data
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<PhoneModel> phones = realm.where(PhoneModel.class).equalTo("type","mobile").findAll();
                phones.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Mobile phones deleted!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void deletePerson(final int id){
        //Delete person according to id
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PersonModel person = realm.where(PersonModel.class).equalTo("id",id).findFirst();
                person.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "PersonModel deleted successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext,"Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public RealmResults<PetModel> queryPets(){
        //Query all the pets
        RealmResults<PetModel> pets = mRealm.where(PetModel.class).findAll();
        return pets;
    }

    public RealmResults<PhoneModel> queryLandlinePhone(){
        //Query phone whose type is landline
        RealmResults<PhoneModel> phones = mRealm.where(PhoneModel.class).equalTo("type","Landline").findAll();
        return phones;
    }

    public RealmResults<PhoneModel> queryMobilePhone(){
        //Query phone whose type is mobile
        RealmResults<PhoneModel> phones = mRealm.where(PhoneModel.class).equalTo("type","mobile").findAll();
        return phones;
    }

    public PersonModel getPerson(int id){
        PersonModel person = mRealm.where(PersonModel.class).equalTo("id",id).findFirst();
        return person;
    }

    public void closeRealm(){
        mRealm.close();
    }

    public void removeChangeListener(){
        personRealmResults.removeChangeListener(changeListener);
    }
}
