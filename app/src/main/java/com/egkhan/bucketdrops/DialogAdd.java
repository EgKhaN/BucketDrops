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
            dismiss();
        }
    };
    public DialogAdd() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputWhat = (EditText) view.findViewById(R.id.et_drop);
        btnClose = (ImageButton) view.findViewById(R.id.btn_close);
        inputWhen = (DatePicker) view.findViewById(R.id.bpv_date);
        btnAdd = (Button) view.findViewById(R.id.btn_add);

        btnClose.setOnClickListener(btnCloseClickListener);
    }
}
