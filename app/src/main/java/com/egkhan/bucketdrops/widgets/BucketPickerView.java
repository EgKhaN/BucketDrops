package com.egkhan.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egkhan.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by EgK on 4.07.2017.
 */

public class BucketPickerView extends LinearLayout{
    Calendar calendar;
    TextView textDate;
    TextView textMonth;
    TextView textYear;
    SimpleDateFormat dateFormat;

    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BucketPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    void init(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view,this);
        calendar = java.util.Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textDate = (TextView) this.findViewById(R.id.tv_date);
        textMonth = (TextView) this.findViewById(R.id.tv_month);
        textYear = (TextView) this.findViewById(R.id.tv_year);
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        update(date,month,year,0,0,0);
    }
    void update(int date,int month,int year,int hour,int minute,int second)
    {
        calendar.set(Calendar.DATE,date);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        textYear.setText(year+"");
        textDate.setText(date+"");
        textMonth.setText(dateFormat.format(calendar.getTime()) +"");
    }
    public long getTime()
    {
        return calendar.getTimeInMillis();
    }
}
