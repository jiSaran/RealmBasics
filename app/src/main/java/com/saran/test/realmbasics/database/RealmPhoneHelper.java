package com.saran.test.realmbasics.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Helper class for various operations in PhoneModel
 */

public class RealmPhoneHelper {

    private Context mContext;
    private Realm mRealm;

    public RealmPhoneHelper(Context context){
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    public PhoneModel createPhone(@NonNull Realm realm, @NonNull PhoneModel phoneModel){
        //insert phone in db and return the object
        PhoneModel mPhone = realm.createObject(PhoneModel.class,phoneModel.getId());
        mPhone.setNumber(phoneModel.getNumber());
        mPhone.setType(phoneModel.getType());
        return mPhone;
    }

    public void insertPhone(@NonNull final PhoneModel phoneModel){
        //insert phone in db
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(phoneModel);
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

    public void insertPhoneList(@NonNull final RealmList<PhoneModel> phoneList){
        //insert phone list in db
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(phoneList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "Phones added successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(mContext, "Error occurred!!!",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    public RealmList<PhoneModel> createPhoneList(@NonNull Realm realm, @NonNull List<PhoneModel> phoneList){
        //insert phone in db and return the objects in list
        if(phoneList!=null && phoneList.size()>0){
            RealmList<PhoneModel> mPhoneList = new RealmList<>();
            for (PhoneModel phone: phoneList) {
                PhoneModel mPhone = realm.createObject(PhoneModel.class,phone.getId());
                mPhone.setNumber(phone.getNumber());
                mPhone.setType(phone.getType());
                mPhoneList.add(mPhone);
            }
            return mPhoneList;
        } else {
            return null;
        }
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

    public RealmResults<PhoneModel> getMobilePhones(){
        //Query phone whose type is mobile
        RealmResults<PhoneModel> phones = mRealm.where(PhoneModel.class).equalTo("type","mobile").findAll();
        return phones;
    }

    public RealmResults<PhoneModel> getLandlinePhones(){
        //Query phone whose type is landline
        RealmResults<PhoneModel> phones = mRealm.where(PhoneModel.class).equalTo("type","Landline").findAll();
        return phones;
    }
}
