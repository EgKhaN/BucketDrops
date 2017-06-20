package com.egkhan.bucketdrops.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.egkhan.bucketdrops.R;

/**
 * Created by EgK on 20.06.2017.
 */

public class Divider extends RecyclerView.ItemDecoration {
    Drawable divider;
    int orientation;

    public Divider(Context context,int orientation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            divider = context.getDrawable(R.drawable.divider);
        else
            divider = ContextCompat.getDrawable(context,R.drawable.divider);

        if(orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("This item Decoration can be used only with RecyclerView that uses a LinearLayoutManager with vertical orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(orientation == LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c,parent,state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left,top,right,bottom;
        left = parent.getPaddingLeft();
        right = parent.getWidth() - parent.getPaddingRight();
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            if(AdapterDrops.FOOTER!=  parent.getAdapter().getItemViewType(i)) {
                View current = parent.getChildAt(i);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) current.getLayoutParams();
                top = current.getTop() - layoutParams.topMargin;
                //top = current.getBottom() + layoutParams.bottomMargin;
                bottom = top + divider.getIntrinsicHeight();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(orientation == LinearLayoutManager.VERTICAL)
        {
            outRect.set(0,0,0,divider.getIntrinsicHeight());
        }
    }
}
