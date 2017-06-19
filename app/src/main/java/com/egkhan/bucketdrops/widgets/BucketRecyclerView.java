package com.egkhan.bucketdrops.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.egkhan.bucketdrops.extras.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by EgK on 20.06.2017.
 */

public class BucketRecyclerView extends RecyclerView {
    List<View> nonEmptyViews = Collections.emptyList();
    List<View> emptyViews = Collections.emptyList();

    private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            toggleViews();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            toggleViews();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            toggleViews();
        }
    };

    private void toggleViews() {
        if (getAdapter() != null && !emptyViews.isEmpty() && !nonEmptyViews.isEmpty())
            if (getAdapter().getItemCount() == 0) {
                setVisibility(View.GONE);
                Util.showViews(emptyViews);
                Util.hideViews(nonEmptyViews);
            } else {
                setVisibility(View.VISIBLE);
                Util.showViews(nonEmptyViews);
                Util.hideViews(emptyViews);
            }
    }

    public BucketRecyclerView(Context context) {
        super(context);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BucketRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null)
            adapter.registerAdapterDataObserver(adapterDataObserver);
        adapterDataObserver.onChanged();
    }

    public void hideIfEmpty(View... views) {
        nonEmptyViews = Arrays.asList(views);
    }

    public void showIfEmpty(View... views) {
        emptyViews = Arrays.asList(views);

    }
}
