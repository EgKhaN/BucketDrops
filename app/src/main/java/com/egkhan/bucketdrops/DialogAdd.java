package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.egkhan.bucketdrops.beans.Drop;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by EgK on 25.05.2017.
 */

public class DialogAdd extends DialogFragment {
    ImageButton btnClose;
    EditText inputWhat;
    DatePicker inputWhen;
    Button btnAdd;

    View.OnClickListener btnCloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_add_it:
                    addAction();
                    break;
            }
            dismiss();
        }
    };
//TODO:
    private void addAction() {
        String what = inputWhat.getText().toString();
        long now = System.currentTimeMillis();
        Realm.init(getActivity());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        Drop drop = new Drop(what, now, 0, false);
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }

    public DialogAdd() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputWhat = (EditText) view.findViewById(R.id.et_drop);
        btnClose = (ImageButton) view.findViewById(R.id.btn_close);
        inputWhen = (DatePicker) view.findViewById(R.id.bpv_date);
        btnAdd = (Button) view.findViewById(R.id.btn_add_it);

        btnClose.setOnClickListener(btnCloseClickListener);
        btnAdd.setOnClickListener(btnCloseClickListener);
    }
}
