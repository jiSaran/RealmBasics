package com.saran.test.realmbasics;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saran.test.realmbasics.database.RealmDBHelper;
import com.saran.test.realmbasics.database.OnPersonSizeChangedListener;
import com.saran.test.realmbasics.database.PersonModel;
import com.saran.test.realmbasics.database.PetModel;
import com.saran.test.realmbasics.database.PhoneModel;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnPersonSizeChangedListener {
    private LinearLayout llContent;
    private Button btnAdd,btnView,btnDelete,btnUpdate;
    private RealmDBHelper dbHelper;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new RealmDBHelper(this,this);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

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
        dbHelper.removeChangeListener();
        dbHelper.closeRealm();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:{
                addData(1);
                break;
            }
            case R.id.btn_update:{
                update(2);
                break;
            }
            case R.id.btn_view:{
                viewData();
                break;
            }
            case R.id.btn_delete:{
                deleteData(1);
                break;
            }
        }
    }

    private void deleteData(int i) {
        switch (i){
            case 1:{
                dbHelper.deletePerson(1);
                break;
            }
            case 2:{
                dbHelper.deleteMobilePhones();
                break;
            }
        }
    }

    private void update(int id){
        switch (id){
            case 1:{
                RealmList<PetModel> pets = new RealmList<>();
                RealmList<PhoneModel> phones = new RealmList<>();

                for(int i=0; i<3; i++){
                    PetModel pet = new PetModel();
                    pet.setId(i+1);
                    pet.setName("Tommy"+i);
                    pet.setType("Dog"+i);
                    pet.setOrigin("Mountains"+i);
                    pets.add(pet);

                    PhoneModel phone = new PhoneModel();
                    phone.setId(i+1);
                    phone.setNumber("01223456");
                    phone.setType("Landline");
                    phones.add(phone);
                }
                PersonModel personModel = new PersonModel();
                personModel.setId(1);
                personModel.setAge(30);
                personModel.setName("Peter");
                personModel.setPets(pets);
                personModel.setPhones(phones);
                dbHelper.updatePerson(personModel);
                break;
            }
            case 2:{
                PetModel pet = new PetModel();
                pet.setId(1);
                pet.setName("Molly");
                pet.setType("Snake");
                pet.setOrigin("Jungle");
                dbHelper.updatePet(pet);
                break;
            }
        }
    }

    private void addData(int id) {
        switch (id){
            case 1:{
                List<PetModel> pets = new ArrayList<>();
                List<PhoneModel> phones = new ArrayList<>();

                for(int i=0; i<4; i++){
                    PetModel pet = new PetModel();
                    pet.setName("Tommy"+i);
                    pet.setType("Dog"+i);
                    pet.setOrigin("Mountains"+i);
                    pets.add(pet);

                    PhoneModel phone = new PhoneModel();
                    phone.setNumber("01223456");
                    phone.setType("Landline");
                    phones.add(phone);
                }
                dbHelper.addPerson("Peter",24,pets,phones);
                break;
            }

            case 2:{
                RealmList<PetModel> pets = new RealmList<>();
                int pt_id = pref.getInt("pt_id",0);
                for(int i=0; i<2; i++){
                    pt_id++;
                    PetModel pet = new PetModel();
                    pet.setId(pt_id);
                    pet.setName("@Tauro"+i);
                    pet.setType("Bull"+i);
                    pet.setOrigin("Hills"+i);
                    pets.add(pet);
                }
                dbHelper.addPets(pets);
                pref.edit().putInt("pt_id",pt_id).commit();
                break;
            }
            case 3:{
                PhoneModel phone = new PhoneModel();
                int ph_id = pref.getInt("ph_id",0);
                ph_id++;
                phone.setId(ph_id);
                phone.setNumber("981111222");
                phone.setType("mobile");
                dbHelper.addPhone(phone);
                pref.edit().putInt("ph_id",ph_id).commit();
                break;
            }
        }

    }

    private void viewData() {
        ViewDataFragment viewDataFragment = ViewDataFragment.getInstance(this, new ViewDataFragment.OnDialogSetListenter() {
            @Override
            public void onDialogSet(long index) {
                if(index == 0){
                    RealmResults<PetModel> pets = dbHelper.queryPets();
                    llContent.removeAllViews();
                    if(pets!=null && pets.size()>0){
                        for(int i = 0; i<pets.size(); i++){
                            populateData(pets.get(i).getName());
                        }
                    }
                } else if(index == 1){
                    RealmResults<PhoneModel> phones = dbHelper.queryLandlinePhone();
                    llContent.removeAllViews();
                    if(phones!=null && phones.size()>0){
                        for (int i=0; i<phones.size(); i++){
                            populateData(phones.get(i).getNumber());
                        }
                    }
                } else if(index == 2){
                    RealmResults<PhoneModel> phones = dbHelper.queryMobilePhone();
                    llContent.removeAllViews();
                    if(phones!=null && phones.size()>0){
                        for (int i=0; i<phones.size(); i++){
                            populateData(phones.get(i).getNumber());
                        }
                    }
                } else if(index == 3){
                    PersonModel person = dbHelper.getPerson(1);
                    llContent.removeAllViews();
                    if(person!=null){
                        populateData(((Integer)person.getId()).toString());
                        populateData(person.getName());
                        populateData(((Integer)person.getAge()).toString());
                        populateData("Pets");
                        for(int i=0; i<person.getPets().size();i++){
                            populateData(person.getPets().get(i).getName());
                        }
                        populateData("Phone");
                        for (int i=0; i<person.getPhones().size(); i++){
                            populateData(person.getPhones().get(i).getNumber());
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
    public void onDataChangedListener(RealmResults<PersonModel> personRealmResults) {
        llContent.removeAllViews();
        for(int i=0; i<personRealmResults.size(); i++){
            populateData(personRealmResults.get(i).getName());
        }
    }
}
