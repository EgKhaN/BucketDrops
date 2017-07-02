package com.egkhan.bucketdrops;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.egkhan.bucketdrops.adapters.CompleteListener;

/**
 * Created by EgK on 24.06.2017.
 */

public class DialogMark extends DialogFragment {
    ImageButton btnClose;
    Button btnCompleted;
    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_completed:
                    markAsComplete();
                    break;
            }
            dismiss();
        }


    };
    private CompleteListener completeListener;

    private void markAsComplete() {
        Bundle arguments = getArguments();

        if(completeListener!=null && arguments!=null) {
            int position = arguments.getInt("POSITION");
            completeListener.onComplete(position);
        }
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnClose = (ImageButton) view.findViewById(R.id.btn_close);
        btnCompleted = (Button) view.findViewById(R.id.btn_completed);
        btnClose.setOnClickListener(btnClickListener);
        btnCompleted.setOnClickListener(btnClickListener);


    }

    public void setCompleteListener(CompleteListener completeListener) {
        this.completeListener = completeListener;

    }
}
