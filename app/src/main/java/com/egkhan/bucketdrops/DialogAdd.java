package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.egkhan.bucketdrops.beans.Drop;
import com.egkhan.bucketdrops.widgets.BucketPickerView;

import io.realm.Realm;


/**
 * Created by EgK on 25.05.2017.
 */

public class DialogAdd extends DialogFragment {
    ImageButton btnClose;
    EditText inputWhat;
    BucketPickerView inputWhen;
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

       // String date = inputWhen.getDayOfMonth() + "/"+ inputWhen.getMonth()+ "/"+inputWhen.getYear();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH,inputWhen.getDayOfMonth());
//        calendar.set(Calendar.MONTH,inputWhen.getMonth());
//        calendar.set(Calendar.YEAR,inputWhen.getYear());
//        calendar.set(Calendar.HOUR,0);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.SECOND,0);

        long now = System.currentTimeMillis();

        Realm realm = Realm.getDefaultInstance();
        Drop drop = new Drop(what, now, inputWhen.getTime(), false);
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }

    public DialogAdd() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
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
        inputWhen = (BucketPickerView) view.findViewById(R.id.bpv_date);
        btnAdd = (Button) view.findViewById(R.id.btn_add_it);

        btnClose.setOnClickListener(btnCloseClickListener);
        btnAdd.setOnClickListener(btnCloseClickListener);
    }
}
