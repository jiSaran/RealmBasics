package com.saran.test.realmtest.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.saran.test.realmtest.MyApplication;
import com.saran.test.realmtest.OnPersonSizeChangedListener;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by core I5 on 5/25/2017.
 */

public class MyDB {
    private Context mContext;
    private Realm mRealm;
    private SharedPreferences pref;
    private RealmChangeListener<RealmResults<Person>> changeListener;
    private RealmResults<Person> personRealmResults;

    public MyDB(Context context, final OnPersonSizeChangedListener listener){
        mContext = context;
        mRealm = MyApplication.getRealmInstance();
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);

        changeListener = new RealmChangeListener<RealmResults<Person>>() {
            @Override
            public void onChange(RealmResults<Person> o) {
                listener.onDataChangedListener(o);
            }
        };
        personRealmResults = mRealm.where(Person.class).findAllSortedAsync("name");
        personRealmResults.addChangeListener(changeListener);
    }

    public void addPerson(@NonNull final String name, @NonNull final int age, @NonNull final List<Pet> pets, @NonNull final List<Phone> phones){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (pref != null) {
                    int id = pref.getInt("id", 0) + 1;
                    Person person = realm.createObject(Person.class, id); //Persisted on db
                    person.setName(name+id);
                    person.setAge(age);
                    for (int i=0; i<pets.size(); i++){
                        Pet pet = realm.createObject(Pet.class, UUID.randomUUID().toString()); //UUID generates unique string
                        pet.setName(pets.get(i).getName());
                        pet.setType(pets.get(i).getType());
                        pet.setOrigin(pets.get(i).getOrigin());
                        person.getPets().add(pet); // get List and add pet
                    }
                    for (int i=0; i<phones.size(); i++){
                        Phone phone = realm.createObject(Phone.class);
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
                Toast.makeText(mContext, "Person added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void addPets(final RealmList<Pet> petList){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(petList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Pet added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public void addPhone(final Phone phone){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(phone); //persisted on db
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Phone added successfully!!!", Toast.LENGTH_SHORT).show();
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
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("id",id).findFirst(); //query should be inside transaction because it can only be changed in same thread
                person.setName(name);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Person updated successfully!!!", Toast.LENGTH_SHORT).show();
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
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Phone> phones = realm.where(Phone.class).equalTo("type","mobile").findAll();
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
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("id",id).findFirst();
                person.deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Person deleted successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext,"Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public RealmResults<Pet> queryPets(){
        RealmResults<Pet> pets = mRealm.where(Pet.class).findAll();
        return pets;
    }

    public RealmResults<Phone> queryLandlinePhone(){
        RealmResults<Phone> phones = mRealm.where(Phone.class).equalTo("type","Landline").findAll();
        return phones;
    }

    public RealmResults<Phone> queryMobilePhone(){
        RealmResults<Phone> phones = mRealm.where(Phone.class).equalTo("type","mobile").findAll();
        return phones;
    }

    public void closeRealm(){
        mRealm.close();
    }

    public void removeChangeListener(){
        personRealmResults.removeChangeListener(changeListener);
    }
}
