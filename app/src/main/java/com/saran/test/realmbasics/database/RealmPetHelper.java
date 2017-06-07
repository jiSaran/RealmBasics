package com.saran.test.realmbasics.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Helper class for various operations in PetModel
 */

public class RealmPetHelper {

    private Context mContext;
    private Realm mRealm;

    public RealmPetHelper(Context context){
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    public RealmList<PetModel> createPetList(@NonNull Realm realm, @NonNull List<PetModel> petList){
        if(petList!=null && petList.size()>0){
            RealmList<PetModel> mPetList = new RealmList<>();
            for (PetModel pet: petList) {
                mPetList.add(realm.copyToRealm(pet));
            }
            return mPetList;
        } else {
            return null;
        }
    }

    public PetModel createPet(@NonNull Realm realm, @NonNull PetModel pet){
        return realm.copyToRealm(pet);
    }

    public void insertPetList(@NonNull final List<PetModel> petList){
        if(petList!=null && petList.size()>0){
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    for (PetModel pet : petList) {
                        realm.copyToRealm(pet);
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Toast.makeText(mContext, "Pets added successfully!!!", Toast.LENGTH_SHORT).show();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
        }
    }

    public void insertPet(@NonNull final PetModel pet){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(pet);
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

    public void updatePet(final PetModel mPet){
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PetModel pet = realm.where(PetModel.class).equalTo("id", mPet.getId()).findFirst();
                pet.setName(mPet.getName());
                pet.setType(mPet.getType());
                pet.setOrigin(mPet.getOrigin());
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Pet updated successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public RealmResults<PetModel> getPets(){
        //Query all the pets
        RealmResults<PetModel> pets = mRealm.where(PetModel.class).findAll();
        return pets;
    }
}
