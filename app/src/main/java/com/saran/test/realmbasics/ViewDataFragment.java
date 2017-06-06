package com.saran.test.realmbasics;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

/** Dialog that helps in selecting the object to be viewed **/

public class ViewDataFragment extends DialogFragment implements View.OnClickListener {

    private OnDialogSetListenter dialogSetListenter;
    private Button btnOkView, btnCancelView;
    private Spinner spViewData;
    private ArrayList<String> list = new ArrayList<>(Arrays.asList("Pets","LandlinePhones","MobilePhones","Person","Multi-person"));

    public ViewDataFragment(){}

    public static ViewDataFragment getInstance(Context context,OnDialogSetListenter onDialogSetListenter){
        ViewDataFragment viewDataFragment = new ViewDataFragment();
        viewDataFragment.init(onDialogSetListenter);
        return viewDataFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view_data,container,false);
        btnOkView = (Button)view.findViewById(R.id.btn_ok_view);
        btnCancelView = (Button)view.findViewById(R.id.btn_cancel_view);
        spViewData = (Spinner)view.findViewById(R.id.sp_view_data);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,list);
        spViewData.setAdapter(arrayAdapter);

        btnOkView.setOnClickListener(this);
        btnCancelView.setOnClickListener(this);
        return view;
    }

    private void init(OnDialogSetListenter onDialogSetListenter) {
        dialogSetListenter = onDialogSetListenter;
    }

    public void notifyDialogSet(){
        if(dialogSetListenter!=null){
            dialogSetListenter.onDialogSet(spViewData.getSelectedItemId());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnOkView.getId()){
            notifyDialogSet();
            dismiss();
        } else if(v.getId() == btnCancelView.getId()){
            if(getDialog()!=null){
                getDialog().cancel();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public interface OnDialogSetListenter{
        void onDialogSet(long index);
    }
}
