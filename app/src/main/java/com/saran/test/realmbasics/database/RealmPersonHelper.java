package com.saran.test.realmbasics.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Helper class for various operations in PersonModel
 */

public class RealmPersonHelper {

    private Context mContext;
    private Realm mRealm;
    private RealmPhoneHelper phoneHelper;
    private RealmPetHelper petHelper;
    private RealmChangeListener<RealmResults<PersonModel>> changeListener;
    private RealmResults<PersonModel> personRealmResults;
    private SharedPreferences pref;

    public RealmPersonHelper(Context context,final OnPersonSizeChangedListener listener){
        mContext = context;
        mRealm = Realm.getDefaultInstance();
        phoneHelper = new RealmPhoneHelper(context);
        petHelper = new RealmPetHelper(context);
        pref = PreferenceManager.getDefaultSharedPreferences(context);

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

    public void insertPerson(@NonNull final String name, @NonNull final int age, @NonNull final List<PetModel> pets, @NonNull final List<PhoneModel> phones){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (pref != null) {
                    int id = pref.getInt("id", 0) + 1;
                    PersonModel person = realm.createObject(PersonModel.class, id); //Persisted on db
                    person.setName(name+id);
                    person.setAge(age);
                    person.setPets(petHelper.createPetList(realm,pets));
                    person.setPhones(phoneHelper.createPhoneList(realm,phones));
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

    public void updatePerson(final PersonModel personModel) {
        //Update person name according to id
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PersonModel person = realm.where(PersonModel.class).equalTo("id",personModel.getId()).findFirst(); //query should be inside transaction because update should be done in same thread
                person.setName(personModel.getName());
                person.setAge(personModel.getAge());
                int phoneListSize = person.getPhones().size();
                if(phoneListSize>=personModel.getPhones().size()){
                    for (int i=0; i<phoneListSize; i++){
                        int delete_index = 0;
                        if(i<personModel.getPhones().size()){
                            person.getPhones().set(i,personModel.getPhones().get(i));
                            delete_index = i+1;
                        } else {
                            person.getPhones().get(delete_index).deleteFromRealm();
                        }
                    }
                } else {
                    int ph_id = pref.getInt("ph_id",0);
                    boolean ph_id_changed = false;
                    for (int i=0; i<personModel.getPhones().size(); i++){
                        if(i>=phoneListSize){
                            ph_id++;
                            PhoneModel phone = realm.createObject(PhoneModel.class,ph_id);
                            phone.setType(personModel.getPhones().get(i).getType());
                            phone.setNumber(personModel.getPhones().get(i).getNumber());
                            ph_id_changed = true;
                            person.getPhones().add(phone);
                        } else{
                            person.getPhones().set(i,personModel.getPhones().get(i));
                        }
                    }
                    if(ph_id_changed){
                        pref.edit().putInt("ph_id",ph_id).commit();
                    }
                }

                int petListSize = person.getPets().size();
                if (petListSize>=personModel.getPets().size()){
                    for (int i=0; i<petListSize; i++){
                        int delete_index = 0;
                        if(i<personModel.getPets().size()){
                            delete_index = i+1;
                            person.getPets().set(i,personModel.getPets().get(i));
                        } else {
                            person.getPets().get(delete_index).deleteFromRealm();
                        }
                    }
                } else {
                    int pt_id = pref.getInt("pt_id",0);
                    boolean pt_id_changed = false;
                    for(int i=0; i<personModel.getPets().size(); i++){
                        if(i>=petListSize){
                            pt_id++;
                            PetModel pet = realm.createObject(PetModel.class,pt_id);
                            pet.setName(personModel.getPets().get(i).getName());
                            pet.setType(personModel.getPets().get(i).getType());
                            pet.setOrigin(personModel.getPets().get(i).getOrigin());
                            pt_id_changed = true;
                            person.getPets().add(pet);
                        } else {
                            person.getPets().set(i,personModel.getPets().get(i));
                        }

                    }
                    if(pt_id_changed){
                        pref.edit().putInt("pt_id",pt_id).commit();
                    }
                }
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

    public void deletePerson(final int id){
        //Delete person according to id
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PersonModel person = realm.where(PersonModel.class).equalTo("id",id).findFirst();
                person.getPhones().deleteAllFromRealm();
                person.getPets().deleteAllFromRealm();
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

    public PersonModel getPerson(int id){
        //Query person whose id is given id
        PersonModel person = mRealm.where(PersonModel.class).equalTo("id",id).findFirst();
        return person;
    }

    public RealmResults<PersonModel> getPersonBetweenAge(int fromAge, int toAge){
        //Query persons whose age is in between given boundary ages
        RealmResults<PersonModel> persons = mRealm.where(PersonModel.class).between("age",fromAge,toAge).findAll();
        return persons;
    }

    public void closeRealm(){
        mRealm.close();
        petHelper.closeRealm();
        phoneHelper.closeRealm();
    }

    public void removeChangeListener(){
        personRealmResults.removeChangeListener(changeListener);
    }

}
