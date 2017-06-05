package com.saran.test.realmtest;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saran.test.realmtest.database.MyDB;
import com.saran.test.realmtest.database.Person;
import com.saran.test.realmtest.database.Pet;
import com.saran.test.realmtest.database.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnPersonSizeChangedListener {
    private LinearLayout llContent;
    private Button btnAdd,btnView,btnDelete,btnUpdate;
    private MyDB myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new MyDB(this,this);

        llContent = (LinearLayout)findViewById(R.id.ll_content);
        btnAdd = (Button)findViewById(R.id.btn_add);
        btnView = (Button)findViewById(R.id.btn_view);
        btnDelete = (Button)findViewById(R.id.btn_delete);
        btnUpdate = (Button)findViewById(R.id.btn_update);

        btnAdd.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        myDB.removeChangeListener();
        myDB.closeRealm();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == btnAdd.getId()){
            addData(3);
        } else if(view.getId() == btnView.getId()){
            viewData();
        } else if(view.getId() == btnDelete.getId()){
            deleteData(1);
        } else if(view.getId() == btnUpdate.getId()){
            myDB.updatePerson(3,"John");
        }
    }

    private void deleteData(int i) {
        switch (i){
            case 1:{
                myDB.deletePerson(1);
            }
            case 2:{
                myDB.deleteMobilePhones();
            }
        }
    }

    private void addData(int id) {
        switch (id){
            case 1:{
                List<Pet> pets = new ArrayList<>();
                List<Phone> phones = new ArrayList<>();

                for(int i=0; i<3; i++){
                    Pet pet = new Pet();
                    pet.setName("Tommy"+i);
                    pet.setType("Dog"+i);
                    pet.setOrigin("Mountains"+i);
                    pets.add(pet);

                    Phone phone = new Phone();
                    phone.setNumber("01223456");
                    phone.setType("Landline");
                    phones.add(phone);
                }
                myDB.addPerson("Peter",24,pets,phones);
                break;
            }

            case 2:{
                RealmList<Pet> pets = new RealmList<>();
                for(int i=0; i<2; i++){
                    Pet pet = new Pet();
                    pet.setId(UUID.randomUUID().toString());
                    pet.setName("@Tauro"+i);
                    pet.setType("Bull"+i);
                    pet.setOrigin("Hills"+i);
                    pets.add(pet);
                }
                myDB.addPets(pets);
                break;
            }
            case 3:{
                Phone phone = new Phone();
                phone.setNumber("981111222");
                phone.setType("mobile");
                myDB.addPhone(phone);
                break;
            }
        }

    }

    private void viewData() {
        ViewDataFragment viewDataFragment = ViewDataFragment.getInstance(this, new ViewDataFragment.OnDialogSetListenter() {
            @Override
            public void onDialogSet(ViewDataFragment viewDataFragment, long index) {
                if(index == 0){
                    RealmResults<Pet> pets = myDB.queryPets();
                    llContent.removeAllViews();
                    if(pets!=null && pets.size()>0){
                        for(int i = 0; i<pets.size(); i++){
                            populateData(pets.get(i).getName());
                        }
                    }
                } else if(index == 1){
                    RealmResults<Phone> phones = myDB.queryLandlinePhone();
                    llContent.removeAllViews();
                    if(phones!=null && phones.size()>0){
                        for (int i=0; i<phones.size(); i++){
                            populateData(phones.get(i).getNumber());
                        }
                    }
                } else if(index == 2){
                    RealmResults<Phone> phones = myDB.queryMobilePhone();
                    llContent.removeAllViews();
                    if(phones!=null && phones.size()>0){
                        for (int i=0; i<phones.size(); i++){
                            populateData(phones.get(i).getNumber());
                        }
                    }
                }
            }
        });
        viewDataFragment.show(getSupportFragmentManager(),"Data Fragment");
    }

    private void populateData(String text){
        TextView textView = new TextView(this);
        textView.setText(text);
        llContent.addView(textView);
    }

    @Override
    public void onDataChangedListener(RealmResults<Person> personRealmResults) {
        llContent.removeAllViews();
        for(int i=0; i<personRealmResults.size(); i++){
            populateData(personRealmResults.get(i).getName());
        }
    }
}
