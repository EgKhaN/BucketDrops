package com.egkhan.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.egkhan.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by EgK on 4.07.2017.
 */

public class BucketPickerView extends LinearLayout implements View.OnTouchListener {
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    private static final int MESSAGE_WHAT = 123;
    public static final int DELAY = 250;

    Calendar calendar;
    TextView textDate;
    TextView textMonth;
    TextView textYear;
    SimpleDateFormat dateFormat;
    boolean increment;
    boolean decrement;
    int activeTextViewID;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (increment)
                increment(activeTextViewID);
            if (decrement)
                decrement(activeTextViewID);
            if (increment || decrement)
                handler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            return true;
        }
    });

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

    void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        calendar = java.util.Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMM");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("super",super.onSaveInstanceState());
        bundle.putInt("date",calendar.get(Calendar.DATE));
        bundle.putInt("year",calendar.get(Calendar.YEAR));
        bundle.putInt("month",calendar.get(Calendar.MONTH));
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Parcelable)
        {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("super");
            int date = bundle.getInt("date");
            int month = bundle.getInt("month");
            int year = bundle.getInt("year");

            update(date,month,year,0,0,0);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textDate = (TextView) this.findViewById(R.id.tv_date);
        textMonth = (TextView) this.findViewById(R.id.tv_month);
        textYear = (TextView) this.findViewById(R.id.tv_year);

        textDate.setOnTouchListener(this);
        textMonth.setOnTouchListener(this);
        textYear.setOnTouchListener(this);

        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);
    }

    void update(int date, int month, int year, int hour, int minute, int second) {
        calendar.set(Calendar.DATE, date);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        textYear.setText(year + "");
        textDate.setText(date + "");
        textMonth.setText(dateFormat.format(calendar.getTime()) + "");
    }

    public long getTime() {
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_date:
                processEventsFor(textDate, event);
                break;
            case R.id.tv_month:
                processEventsFor(textMonth, event);
                break;
            case R.id.tv_year:
                processEventsFor(textYear, event);
                break;
        }
//        switch (event.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event) {
        Drawable[] drawables = textView.getCompoundDrawables();
        if (hasDrawableTop(drawables) && hasDrawableBottom(drawables)) {
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            activeTextViewID = textView.getId();
            if (topDrawableHit(textView, topBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    increment = true;
                    increment(textView.getId());
                    handler.removeMessages(MESSAGE_WHAT);
                    handler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView,true);
                }
                if (isActionUpOrCancel(event)) {
                    increment = false;
                    toggleDrawable(textView,false);
                }

            } else if (bottomDrawableHit(textView, bottomBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    decrement = true;
                    decrement(textView.getId());
                    handler.removeMessages(MESSAGE_WHAT);
                    handler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                    toggleDrawable(textView,true);
                }
                if (isActionUpOrCancel(event)) {
                    decrement = false;
                    toggleDrawable(textView,false);
                }
            } else {
                increment = false;
                decrement = false;
                toggleDrawable(textView,false);
            }
        }
    }

    void increment(int textViewId) {
        switch (textViewId) {
            case R.id.tv_date:
                calendar.add(Calendar.DATE, 1);
                break;
            case R.id.tv_month:
                calendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tv_year:
                calendar.add(Calendar.YEAR, 1);
                break;
        }
        setWidgetDate(calendar);
    }

    private void setWidgetDate(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);

        textDate.setText(date + "");
        textMonth.setText(dateFormat.format(calendar.getTime()));
        textYear.setText(year + "");
    }

    void decrement(int textViewId) {
        switch (textViewId) {
            case R.id.tv_date:
                calendar.add(Calendar.DATE, -1);
                break;
            case R.id.tv_month:
                calendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_year:
                calendar.add(Calendar.YEAR, -1);
                break;
        }
        setWidgetDate(calendar);
    }

    boolean isActionDown(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    boolean isActionUpOrCancel(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMin = textView.getPaddingTop();
        int yMax = textView.getPaddingTop() + drawableHeight;
        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xMin = textView.getPaddingLeft();
        int xMax = textView.getWidth() - textView.getPaddingRight();
        int yMax = textView.getHeight() - textView.getPaddingBottom();
        int yMin = yMax - drawableHeight;
        return x > xMin && x < xMax && y > yMin && y < yMax;
    }

    boolean hasDrawableTop(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    boolean hasDrawableBottom(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }
    void toggleDrawable(TextView textView,boolean pressed)
    {
        if(pressed)
        {
            if(increment)
            {
textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_pressed,0, R.drawable.down_normal);
            }
            if(decrement)
            {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal,0, R.drawable.down_pressed);
            }
        }
        else{
            textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.up_normal,0, R.drawable.down_normal);
        }
    }
}
